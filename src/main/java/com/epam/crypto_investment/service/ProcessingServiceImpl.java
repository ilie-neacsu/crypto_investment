package com.epam.crypto_investment.service;

import com.epam.crypto_investment.entity.Crypto;
import com.epam.crypto_investment.repository.CryptoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessingServiceImpl implements ProcessingService {

    private final CryptoRepository cryptoRepository;

    public ProcessingServiceImpl(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    @Override
    public void processCryptos(List<Crypto> cryptos) {
        persistCryptos(cryptos);
    }

    private void persistCryptos(List<Crypto> cryptos) {
        cryptoRepository.saveAll(cryptos);
    }
}
