package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoDTO;
import com.epam.crypto_investment.dto.CryptoStatsDTO;

import java.util.List;

public interface CryptoService {
    List<CryptoDTO> getCryptosSortedByNormalizedRange();
    CryptoStatsDTO getCryptoStats(String symbol);
}
