package com.epam.crypto_investment.service;

import com.epam.crypto_investment.domain.Crypto;
import com.epam.crypto_investment.domain.CryptoStats;
import com.epam.crypto_investment.dto.CryptoDTO;
import com.epam.crypto_investment.dto.CryptoStatsDTO;
import com.epam.crypto_investment.entity.CryptoPrice;
import com.epam.crypto_investment.entity.MonthlyStat;
import com.epam.crypto_investment.repository.MonthlyStatRepository;
import com.epam.crypto_investment.service.mapper.CryptoStatsMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final Logger logger = Logger.getLogger(CryptoServiceImpl.class.getName());

    private final MonthlyStatRepository monthlyStatRepository;
    private final CryptoStatsMapper cryptoStatsMapper;

    public CryptoServiceImpl(
            MonthlyStatRepository monthlyStatRepository,
            CryptoStatsMapper cryptoStatsMapper
    ) {
        this.monthlyStatRepository = monthlyStatRepository;
        this.cryptoStatsMapper = cryptoStatsMapper;
    }

    @Override
    public List<CryptoDTO> getCryptosSortedByNormalizedRange() {

        List<String> symbols = monthlyStatRepository.findAllDistinctSymbols();

        logger.info(String.format("Found %d distinct symbols", symbols.size()));

        return symbols.stream()
                .map(this::mapToCryptoDTO)
                .sorted(Comparator.comparingDouble(CryptoDTO::getNormalizedPrice).reversed())
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
                .normalizedPrice(normalizedRange)
                .build();
    }
}
