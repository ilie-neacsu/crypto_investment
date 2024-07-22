package com.epam.crypto_investment.repository;

import com.epam.crypto_investment.entity.DailyStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
}
