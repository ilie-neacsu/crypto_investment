package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.Crypto;

import java.util.List;

public interface ProcessingService {
    void processCryptos(List<Crypto> cryptos);
}
