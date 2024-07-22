package com.epam.crypto_investment.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "daily_stats")
public class DailyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private LocalDate date;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "minimum_id", referencedColumnName = "id")
    private CryptoPrice minimumCryptoPrice;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "maximum_id", referencedColumnName = "id")
    private CryptoPrice maximumCryptoPrice;
}
