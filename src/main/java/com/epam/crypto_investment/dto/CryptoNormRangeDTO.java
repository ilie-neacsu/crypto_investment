package com.epam.crypto_investment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoNormRangeDTO {
    private String symbol;
    private LocalDate date;
    private double normalizedRange;
    private double minPrice;
    private double maxPrice;
}
