package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoPriceDTO;
import com.epam.crypto_investment.entity.CryptoPrice;

public interface CryptoPriceMapper {
    CryptoPrice toEntity(CryptoPriceDTO dto);
    CryptoPriceDTO toDto(CryptoPrice entity);
}
