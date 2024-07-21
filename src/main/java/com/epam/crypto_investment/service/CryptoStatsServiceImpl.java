package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.CryptoPrice;
import com.epam.crypto_investment.entity.MonthlyStat;
import com.epam.crypto_investment.repository.MonthlyStatRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class CryptoStatsServiceImpl implements CryptoStatsService {

    private final BlockingQueue<List<CryptoPrice>> taskQueue = new LinkedBlockingQueue<>();
    private final MonthlyStatRepository monthlyStatRepository;
    private Thread processingThread;

    public CryptoStatsServiceImpl(MonthlyStatRepository monthlyStatRepository) {
        this.monthlyStatRepository = monthlyStatRepository;
    }

    @PostConstruct
    public void init() {
        this.processingThread = Thread.startVirtualThread(this::processQueue);
    }

    private void processQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<CryptoPrice> cryptoPrices = taskQueue.take();
                processMonthlyStat(cryptoPrices);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void queueProcessCryptoStats(List<CryptoPrice> cryptoPrices) {
        taskQueue.offer(cryptoPrices);
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
