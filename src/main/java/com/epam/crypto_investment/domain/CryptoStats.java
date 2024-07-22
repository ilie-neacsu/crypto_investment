package com.epam.crypto_investment.domain;

import com.epam.crypto_investment.entity.CryptoPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoStats {
    private CryptoPrice minimumCryptoPrice;
    private CryptoPrice maximumCryptoPrice;
    private CryptoPrice oldestCryptoPrice;
    private CryptoPrice newestCryptoPrice;
}
