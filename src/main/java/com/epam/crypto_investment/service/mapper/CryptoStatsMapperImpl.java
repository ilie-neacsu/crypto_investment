package com.epam.crypto_investment.service.mapper;

import com.epam.crypto_investment.domain.CryptoStats;
import com.epam.crypto_investment.dto.CryptoStatDTO;
import com.epam.crypto_investment.dto.CryptoStatsDTO;
import com.epam.crypto_investment.entity.MonthlyStat;
import org.springframework.stereotype.Component;

@Component
public class CryptoStatsMapperImpl implements CryptoStatsMapper {

    @Override
    public CryptoStatsDTO toDto(CryptoStats entity) {

        CryptoStatDTO min = CryptoStatDTO.builder()
                .timestamp(entity.getMinimumCryptoPrice().getTimestamp())
                .symbol(entity.getMinimumCryptoPrice().getSymbol())
                .price(entity.getMinimumCryptoPrice().getPrice())
                .build();

        CryptoStatDTO max = CryptoStatDTO.builder()
                .timestamp(entity.getMaximumCryptoPrice().getTimestamp())
                .symbol(entity.getMaximumCryptoPrice().getSymbol())
                .price(entity.getMaximumCryptoPrice().getPrice())
                .build();

        CryptoStatDTO oldest = CryptoStatDTO.builder()
                .timestamp(entity.getOldestCryptoPrice().getTimestamp())
                .symbol(entity.getOldestCryptoPrice().getSymbol())
                .price(entity.getOldestCryptoPrice().getPrice())
                .build();

        CryptoStatDTO newest = CryptoStatDTO.builder()
                .timestamp(entity.getNewestCryptoPrice().getTimestamp())
                .symbol(entity.getNewestCryptoPrice().getSymbol())
                .price(entity.getNewestCryptoPrice().getPrice())
                .build();

        return CryptoStatsDTO.builder()
                .min(min)
                .max(max)
                .oldest(oldest)
                .newest(newest)
                .build();
    }

    @Override
    public CryptoStats toEntity(CryptoStatsDTO dto) {
        throw new UnsupportedOperationException("N/A");
    }

}
