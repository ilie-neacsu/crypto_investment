package com.epam.crypto_investment.repository;

import com.epam.crypto_investment.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {
    Optional<CryptoPrice> findOldestCryptoPriceBySymbol(String symbol);
}
