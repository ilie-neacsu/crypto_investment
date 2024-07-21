package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.CryptoPrice;
import com.epam.crypto_investment.entity.MonthlyStat;
import com.epam.crypto_investment.repository.CryptoRepository;
import com.epam.crypto_investment.repository.MonthlyStatsRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

@Service
public class ProcessingServiceImpl implements ProcessingService {

    private final CryptoRepository cryptoRepository;
    private final MonthlyStatsRepository monthlyStatsRepository;

    public ProcessingServiceImpl(
            CryptoRepository cryptoRepository,
            MonthlyStatsRepository monthlyStatsRepository
    ) {
        this.cryptoRepository = cryptoRepository;
        this.monthlyStatsRepository = monthlyStatsRepository;
    }

    @Override
    public void processCryptoPrices(List<CryptoPrice> cryptos) {
        List<CryptoPrice> persistedCryptoPrices = persistCryptoPrices(cryptos);
        processMonthlyStat(persistedCryptoPrices);
    }

    @Async
    protected void processMonthlyStat(List<CryptoPrice> cryptos) {

        CryptoPrice cryptoPrice = cryptos.getFirst();

        YearMonth batchYearMonth = YearMonth.from(cryptoPrice.getTimestamp().toLocalDate());
        String batchSymbol = cryptoPrice.getSymbol();

        CryptoPrice minimumCryptoPrice = cryptos.stream()
                .min(Comparator.comparing(CryptoPrice::getPrice))
                .orElseThrow();

        CryptoPrice maximumCryptoPrice = cryptos.stream()
                .max(Comparator.comparing(CryptoPrice::getPrice))
                .orElseThrow();

        CryptoPrice oldestCryptoPrice = cryptos.stream()
                .min(Comparator.comparing(CryptoPrice::getTimestamp))
                .orElseThrow();

        CryptoPrice newestCryptoPrice = cryptos.stream()
                .max(Comparator.comparing(CryptoPrice::getTimestamp))
                .orElseThrow();

        MonthlyStat monthlyStat = new MonthlyStat();

        monthlyStat.setMonthYear(batchYearMonth);
        monthlyStat.setSymbol(batchSymbol);
        monthlyStat.setMinimumCryptoPrice(minimumCryptoPrice);
        monthlyStat.setMaximumCryptoPrice(maximumCryptoPrice);
        monthlyStat.setOldestCryptoPrice(oldestCryptoPrice);
        monthlyStat.setNewestCryptoPrice(newestCryptoPrice);

        persistMonthlyStat(monthlyStat);
    }

    private List<CryptoPrice> persistCryptoPrices(List<CryptoPrice> cryptos) {
        return cryptoRepository.saveAll(cryptos);
    }

    private void persistMonthlyStat(MonthlyStat monthlyStat) {
        monthlyStatsRepository.save(monthlyStat);
    }
}
