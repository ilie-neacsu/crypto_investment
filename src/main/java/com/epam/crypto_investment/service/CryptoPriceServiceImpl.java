package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.CryptoPrice;
import com.epam.crypto_investment.repository.CryptoPriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoPriceServiceImpl implements CryptoPriceService {

    private final CryptoPriceRepository cryptoPriceRepository;
    private final CryptoStatsService cryptoStatsService;

    public CryptoPriceServiceImpl(
            CryptoPriceRepository cryptoPriceRepository,
            CryptoStatsService cryptoStatsService
    ) {
        this.cryptoPriceRepository = cryptoPriceRepository;
        this.cryptoStatsService = cryptoStatsService;
    }

    @Override
    public void saveCryptoPrices(List<CryptoPrice> cryptos) {
        List<CryptoPrice> persistedCryptos = cryptoPriceRepository.saveAll(cryptos);
        cryptoStatsService.queueProcessCryptoStats(persistedCryptos);
    }
}
