package com.epam.crypto_investment.repository;

import com.epam.crypto_investment.entity.MonthlyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MonthlyStatRepository extends JpaRepository<MonthlyStat, Long> {
    List<MonthlyStat> findBySymbol(String symbol);
}
