package com.epam.crypto_investment.service.mapper;

import com.epam.crypto_investment.domain.Crypto;
import com.epam.crypto_investment.dto.CryptoDTO;

public class CryptoMapperImpl implements CryptoMapper {

    @Override
    public Crypto toEntity(CryptoDTO dto) {

        return  dto != null ? Crypto.builder()
                .symbol(dto.getSymbol())
                .normalizedPrice(dto.getNormalizedRange())
                .build() : null;
    }

    @Override
    public CryptoDTO toDto(Crypto entity) {

        return entity != null ? CryptoDTO.builder()
                .symbol(entity.getSymbol())
                .normalizedRange(entity.getNormalizedPrice())
                .build() : null;
    }
}
