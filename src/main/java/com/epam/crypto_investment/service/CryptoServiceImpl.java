package com.epam.crypto_investment.service;

import com.epam.crypto_investment.domain.CryptoStats;
import com.epam.crypto_investment.dto.CryptoDTO;
import com.epam.crypto_investment.dto.CryptoNormRangeDTO;
import com.epam.crypto_investment.dto.CryptoStatsDTO;
import com.epam.crypto_investment.entity.CryptoPrice;
import com.epam.crypto_investment.entity.DailyStat;
import com.epam.crypto_investment.entity.MonthlyStat;
import com.epam.crypto_investment.repository.DailyStatRepository;
import com.epam.crypto_investment.repository.MonthlyStatRepository;
import com.epam.crypto_investment.service.mapper.CryptoStatsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final Logger logger = Logger.getLogger(CryptoServiceImpl.class.getName());

    private final DailyStatRepository dailyStatRepository;
    private final MonthlyStatRepository monthlyStatRepository;
    private final CryptoStatsMapper cryptoStatsMapper;

    public CryptoServiceImpl(
            MonthlyStatRepository monthlyStatRepository,
            CryptoStatsMapper cryptoStatsMapper,
            DailyStatRepository dailyStatRepository
    ) {
        this.dailyStatRepository = dailyStatRepository;
        this.monthlyStatRepository = monthlyStatRepository;
        this.cryptoStatsMapper = cryptoStatsMapper;
    }

    @Override
    public List<CryptoDTO> getCryptosSortedByNormalizedRange() {

        List<String> symbols = getDistinctSymbols();

        logger.info(String.format("Found %d distinct symbols", symbols.size()));

        return symbols.stream()
                .map(this::mapToCryptoDTO)
                .sorted(Comparator.comparingDouble(CryptoDTO::getNormalizedRange).reversed())
                .toList();

    }

    @Override
    public CryptoStatsDTO getCryptoStats(String symbol) {

        CryptoPrice minimumCryptoPrice = monthlyStatRepository
                .findCryptoStatWithLowestMinimumBySymbol(symbol)
                .map(MonthlyStat::getMinimumCryptoPrice)
                .orElseThrow();

        CryptoPrice maximumCryptoPrice = monthlyStatRepository
                .findCryptoStatWithHighestMaximumBySymbol(symbol)
                .map(MonthlyStat::getMaximumCryptoPrice)
                .orElseThrow();

        CryptoPrice oldestCryptoPrice = monthlyStatRepository
                .findCryptoStatWithOldestCryptoPriceBySymbol(symbol)
                .map(MonthlyStat::getOldestCryptoPrice)
                .orElseThrow();

        CryptoPrice newestCryptoPrice = monthlyStatRepository
                .findCryptoStatWithNewestCryptoPriceBySymbol(symbol)
                .map(MonthlyStat::getNewestCryptoPrice)
                .orElseThrow();

        CryptoStats cryptoStats = CryptoStats.builder()
                .minimumCryptoPrice(minimumCryptoPrice)
                .maximumCryptoPrice(maximumCryptoPrice)
                .oldestCryptoPrice(oldestCryptoPrice)
                .newestCryptoPrice(newestCryptoPrice)
                .build();


        return cryptoStatsMapper.toDto(cryptoStats);
    }

    @Override
    public CryptoNormRangeDTO getCryptoWithHighestNormalizedRange(LocalDate date) {

        List<String> symbols = getDistinctSymbols();

        return symbols.stream()
                .map(symbol -> getDailyStat(date, symbol))
                .filter(Objects::nonNull)
                .map(this::mapCryptoNormRangeDTO)
                .max(Comparator.comparingDouble(CryptoNormRangeDTO::getNormalizedRange))
                .orElseThrow();
    }

    private DailyStat getDailyStat(LocalDate date, String symbol) {
        return dailyStatRepository
                .findByDateAndSymbol(date, symbol)
                .orElse(null);
    }

    private CryptoNormRangeDTO mapCryptoNormRangeDTO(DailyStat dailyStat) {

        double minPrice = dailyStat.getMinimumCryptoPrice().getPrice();
        double maxPrice = dailyStat.getMaximumCryptoPrice().getPrice();
        double normalizedRange = (maxPrice - minPrice) / minPrice;

        return CryptoNormRangeDTO.builder()
                .symbol(dailyStat.getSymbol())
                .date(dailyStat.getDate())
                .normalizedRange(normalizedRange)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();
    }

    private CryptoDTO mapToCryptoDTO(String symbol) {

        double minPrice = monthlyStatRepository
                .findCryptoStatWithLowestMinimumBySymbol(symbol)
                .map(monthlyStat -> monthlyStat.getMinimumCryptoPrice().getPrice())
                .orElseThrow();

        double maxPrice = monthlyStatRepository
                .findCryptoStatWithHighestMaximumBySymbol(symbol)
                .map(monthlyStat -> monthlyStat.getMaximumCryptoPrice().getPrice())
                .orElseThrow();

        logger.info(String.format("Max price: %f, Min price: %f", maxPrice, minPrice));

        double normalizedRange = (maxPrice - minPrice) / minPrice;

        logger.info(String.format("Normalized range for %s is %f", symbol, normalizedRange));

        return CryptoDTO.builder()
                .symbol(symbol)
                .normalizedRange(normalizedRange)
                .build();
    }

    private List<String> getDistinctSymbols() {
        return monthlyStatRepository.findAllDistinctSymbols();
    }
}
