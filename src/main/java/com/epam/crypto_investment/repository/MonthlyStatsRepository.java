package com.epam.crypto_investment.repository;

import com.epam.crypto_investment.entity.MonthlyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MonthlyStatsRepository extends JpaRepository<MonthlyStat, Long> {
}
