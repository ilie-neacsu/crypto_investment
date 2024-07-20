package com.epam.crypto_investment.repository;

import com.epam.crypto_investment.entity.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepository extends JpaRepository<Crypto, Long> {
}
