package com.epam.crypto_investment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int month;

    private int year;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "minimum_id", referencedColumnName = "id")
    private Crypto minimum;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "maximum_id", referencedColumnName = "id")
    private Crypto maximum;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oldest_id", referencedColumnName = "id")
    private Crypto oldest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "newest_id", referencedColumnName = "id")
    private Crypto newest;
}
