package com.epam.crypto_investment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoStatsDTO {
    private CryptoStatDTO oldest;
    private CryptoStatDTO newest;
    private CryptoStatDTO min;
    private CryptoStatDTO max;
}
