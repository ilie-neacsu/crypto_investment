package com.epam.crypto_investment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monthly_stats")
public class MonthlyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private YearMonth monthYear;

    @Column(nullable = false)
    private String symbol;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "minimum_id", referencedColumnName = "id")
    private CryptoPrice minimumCryptoPrice;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "maximum_id", referencedColumnName = "id")
    private CryptoPrice maximumCryptoPrice;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "oldest_id", referencedColumnName = "id")
    private CryptoPrice oldestCryptoPrice;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "newest_id", referencedColumnName = "id")
    private CryptoPrice newestCryptoPrice;
}
