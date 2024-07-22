package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.CryptoPrice;
import com.epam.crypto_investment.entity.DailyStat;
import com.epam.crypto_investment.entity.MonthlyStat;
import com.epam.crypto_investment.repository.DailyStatRepository;
import com.epam.crypto_investment.repository.MonthlyStatRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
public class CryptoStatsServiceImpl implements CryptoStatsService {

    private final BlockingQueue<List<CryptoPrice>> taskQueue = new LinkedBlockingQueue<>();
    private final MonthlyStatRepository monthlyStatRepository;
    private final DailyStatRepository dailyStatRepository;
    private Thread processingThread;

    public CryptoStatsServiceImpl(
            MonthlyStatRepository monthlyStatRepository,
            DailyStatRepository dailyStatRepository
            ) {
        this.monthlyStatRepository = monthlyStatRepository;
        this.dailyStatRepository = dailyStatRepository;
    }

    @PostConstruct
    public void init() {
        this.processingThread = Thread.startVirtualThread(this::processQueue);
    }

    private void processQueue() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    List<CryptoPrice> cryptoPrices = taskQueue.take();

                    Future<?> monthlyStatFuture = executor.submit(() -> processMonthlyStat(cryptoPrices));
                    Future<?> dailyStatFuture = executor.submit(() -> processDailyStat(cryptoPrices));

                    dailyStatFuture.get();
                    monthlyStatFuture.get();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (ExecutionException e) {
                    throw new RuntimeException(e.getCause());
                }

            }
        }
    }

    @Override
    public void queueProcessCryptoStats(List<CryptoPrice> cryptoPrices) {
        taskQueue.offer(cryptoPrices);
    }

    private void processDailyStat(List<CryptoPrice> cryptoPrices) {

        Map<String, Map<LocalDate, List<CryptoPrice>>> groupedBySymbolAndDate = cryptoPrices.stream()
                .collect(Collectors.groupingBy(CryptoPrice::getSymbol,
                        Collectors.groupingBy(price -> price.getTimestamp().toLocalDate())));

        groupedBySymbolAndDate.forEach((symbol, datePricesMap) -> {
            datePricesMap.forEach((date, prices) -> {
                CryptoPrice minPrice = prices.stream()
                        .min(Comparator.comparingDouble(CryptoPrice::getPrice)).orElseThrow();
                CryptoPrice maxPrice = prices.stream()
                        .max(Comparator.comparingDouble(CryptoPrice::getPrice)).orElseThrow();

                DailyStat dailyStat = DailyStat.builder()
                        .symbol(symbol)
                        .date(date)
                        .minimumCryptoPrice(minPrice)
                        .maximumCryptoPrice(maxPrice)
                        .build();

                dailyStatRepository.save(dailyStat);
            });
        });
    }

    private void processMonthlyStat(List<CryptoPrice> cryptoPrices) {

        CryptoPrice cryptoPrice = cryptoPrices.getFirst();
        YearMonth batchYearMonth = YearMonth.from(cryptoPrice.getTimestamp().toLocalDate());
        String currentBatchSymbol = cryptoPrice.getSymbol();

        CryptoPrice minimumCryptoPrice = cryptoPrices.stream()
                .min(Comparator.comparing(CryptoPrice::getPrice))
                    .orElseThrow();

        CryptoPrice maximumCryptoPrice = cryptoPrices.stream()
                .max(Comparator.comparing(CryptoPrice::getPrice))
                .orElseThrow();

        CryptoPrice oldestCryptoPrice = cryptoPrices.stream()
                .min(Comparator.comparing(CryptoPrice::getTimestamp))
                .orElseThrow();

        CryptoPrice newestCryptoPrice = cryptoPrices.stream()
                .max(Comparator.comparing(CryptoPrice::getTimestamp))
                .orElseThrow();

        MonthlyStat monthlyStat = MonthlyStat.builder()
                .monthYear(batchYearMonth)
                .symbol(currentBatchSymbol)
                .minimumCryptoPrice(minimumCryptoPrice)
                .maximumCryptoPrice(maximumCryptoPrice)
                .oldestCryptoPrice(oldestCryptoPrice)
                .newestCryptoPrice(newestCryptoPrice)
                .build();

        monthlyStatRepository.save(monthlyStat);
    }

    @PreDestroy
    public void shutdown() {
        if (processingThread != null) {
            processingThread.interrupt();
        }
    }
}
