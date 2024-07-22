package com.epam.crypto_investment.service;

import com.epam.crypto_investment.controller.IngestionController;
import com.epam.crypto_investment.dto.CryptoDTO;
import com.epam.crypto_investment.repository.MonthlyStatRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final Logger logger = Logger.getLogger(CryptoServiceImpl.class.getName());

    private final MonthlyStatRepository monthlyStatRepository;

    public CryptoServiceImpl(MonthlyStatRepository monthlyStatRepository) {
        this.monthlyStatRepository = monthlyStatRepository;
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

    private CryptoDTO mapToCryptoDTO(String symbol) {

        double minPrice = monthlyStatRepository
                .findLowestMinimumCryptoPriceBySymbol(symbol)
                .map(monthlyStat -> monthlyStat.getMinimumCryptoPrice().getPrice())
                .orElseThrow();

        double maxPrice = monthlyStatRepository
                .findLowestMaximumCryptoPriceBySymbol(symbol)
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
