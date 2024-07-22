package com.epam.crypto_investment.repository;

import com.epam.crypto_investment.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
    Optional<DailyStat> findByDateAndSymbol(LocalDate date, String symbol);
}
