package com.epam.crypto_investment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoStatDTO {
    private LocalDateTime timestamp;
    private String symbol;
    private double price;
}
