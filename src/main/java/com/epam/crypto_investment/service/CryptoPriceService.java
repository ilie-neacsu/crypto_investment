package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.CryptoPrice;

import java.util.List;

public interface CryptoPriceService {
    void saveCryptoPrices(List<CryptoPrice> cryptoPrices);
}
