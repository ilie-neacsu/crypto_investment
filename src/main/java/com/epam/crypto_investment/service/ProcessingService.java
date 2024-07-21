package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.CryptoPrice;

import java.util.List;

public interface ProcessingService {
    void processCryptoPrices(List<CryptoPrice> cryptos);
}
