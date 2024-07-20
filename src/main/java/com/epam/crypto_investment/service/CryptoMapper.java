package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoDto;
import com.epam.crypto_investment.entity.Crypto;

public interface CryptoMapper {
    Crypto toEntity(CryptoDto dto);
    CryptoDto toDto(Crypto entity);
}
