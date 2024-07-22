package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoDTO;
import com.epam.crypto_investment.dto.CryptoNormRangeDTO;
import com.epam.crypto_investment.dto.CryptoStatsDTO;

import java.time.LocalDate;
import java.util.List;

public interface CryptoService {
    List<CryptoDTO> getCryptosSortedByNormalizedRange();
    CryptoStatsDTO getCryptoStats(String symbol);
    CryptoStatsDTO getCryptoStats(String symbol, LocalDate startDate, LocalDate endDate);
    CryptoNormRangeDTO getCryptoWithHighestNormalizedRange(LocalDate date);
    CryptoStatsDTO getCryptoStatsSixMonths(String symbol);
    CryptoStatsDTO getCryptoStatsYearly(String symbol);
}
