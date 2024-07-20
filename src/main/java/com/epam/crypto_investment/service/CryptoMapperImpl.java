package com.epam.crypto_investment.service;

import com.epam.crypto_investment.dto.CryptoDto;
import com.epam.crypto_investment.entity.Crypto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Component
public class CryptoMapperImpl implements CryptoMapper {

    @Override
    public Crypto toEntity(CryptoDto dto) {
        if (dto == null) {
            return null;
        }

        Crypto crypto = new Crypto();
        crypto.setTimestamp(toLocalDateTime(dto.getTimestamp()));
        crypto.setSymbol(dto.getSymbol());
        crypto.setPrice(dto.getPrice());

        return crypto;
    }

    @Override
    public CryptoDto toDto(Crypto entity) {
        if (entity == null) {
            return null;
        }

        CryptoDto dto = new CryptoDto();
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
