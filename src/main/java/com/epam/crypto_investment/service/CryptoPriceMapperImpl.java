package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoPriceDTO;
import com.epam.crypto_investment.entity.CryptoPrice;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Component
public class CryptoPriceMapperImpl implements CryptoPriceMapper {

    @Override
    public CryptoPrice toEntity(CryptoPriceDTO dto) {
        if (dto == null) {
            return null;
        }

        CryptoPrice crypto = new CryptoPrice();
        crypto.setTimestamp(toLocalDateTime(dto.getTimestamp()));
        crypto.setSymbol(dto.getSymbol());
        crypto.setPrice(dto.getPrice());

        return crypto;
    }

    @Override
    public CryptoPriceDTO toDto(CryptoPrice entity) {
        if (entity == null) {
            return null;
        }

        CryptoPriceDTO dto = new CryptoPriceDTO();
        dto.setTimestamp(toTimestamp(entity.getTimestamp()));
        dto.setSymbol(entity.getSymbol());
        dto.setPrice(entity.getPrice());
        return dto;
    }

    private LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    private long toTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
