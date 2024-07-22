package com.epam.crypto_investment.service.mapper;

import com.epam.crypto_investment.domain.CryptoStats;
import com.epam.crypto_investment.dto.CryptoStatDTO;
import com.epam.crypto_investment.dto.CryptoStatsDTO;
import com.epam.crypto_investment.entity.MonthlyStat;

public interface CryptoStatsMapper {
    CryptoStatsDTO toDto(CryptoStats entity);
    CryptoStats toEntity(CryptoStatsDTO dto);
}
