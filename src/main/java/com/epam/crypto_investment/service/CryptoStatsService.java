package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.CryptoPrice;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CryptoStatsService {
    void queueProcessCryptoStats(List<CryptoPrice> cryptos);
}
