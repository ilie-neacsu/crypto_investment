package com.epam.crypto_investment.service.mapper;

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
        return dto != null ? CryptoPrice.builder()
                .timestamp(toLocalDateTime(dto.getTimestamp()))
                .symbol(dto.getSymbol())
                .price(dto.getPrice())
                .build() : null;
    }

    @Override
    public CryptoPriceDTO toDto(CryptoPrice entity) {
        return entity != null ? CryptoPriceDTO.builder()
                .timestamp(toTimestamp(entity.getTimestamp()))
                .symbol(entity.getSymbol())
                .price(entity.getPrice())
                .build() : null;
    }

    private LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    private long toTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
