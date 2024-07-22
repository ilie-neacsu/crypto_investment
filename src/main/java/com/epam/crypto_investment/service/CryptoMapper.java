package com.epam.crypto_investment.service;

import com.epam.crypto_investment.domain.Crypto;
import com.epam.crypto_investment.dto.CryptoDTO;

public interface CryptoMapper {
    Crypto toEntity(CryptoDTO dto);
    CryptoDTO toDto(Crypto entity);
}
