package com.epam.crypto_investment.repository;

import com.epam.crypto_investment.entity.MonthlyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


@Repository
public interface MonthlyStatRepository extends JpaRepository<MonthlyStat, Long> {

    @Query("SELECT DISTINCT ms.symbol FROM MonthlyStat ms")
    List<String> findAllDistinctSymbols();

    @Query("SELECT ms FROM MonthlyStat ms JOIN ms.minimumCryptoPrice cp " +
            "WHERE ms.symbol = :symbol AND cp.price = " +
            "(SELECT MIN(cpInner.price) FROM MonthlyStat msInner JOIN msInner.minimumCryptoPrice cpInner " +
            "WHERE msInner.symbol = :symbol)")
    Optional<MonthlyStat> findCryptoStatWithLowestMinimumBySymbol(@Param("symbol") String symbol);

    @Query("SELECT ms FROM MonthlyStat ms JOIN ms.maximumCryptoPrice cp " +
            "WHERE ms.symbol = :symbol AND cp.price = " +
            "(SELECT MAX(cpInner.price) FROM MonthlyStat msInner JOIN msInner.maximumCryptoPrice cpInner " +
            "WHERE msInner.symbol = :symbol)")
    Optional<MonthlyStat> findCryptoStatWithHighestMaximumBySymbol(@Param("symbol") String symbol);

    @Query("SELECT ms FROM MonthlyStat ms JOIN ms.oldestCryptoPrice cp " +
            "WHERE ms.symbol = :symbol AND cp.timestamp = " +
            "(SELECT MIN(cpInner.timestamp) FROM MonthlyStat msInner JOIN msInner.oldestCryptoPrice cpInner " +
            "WHERE msInner.symbol = :symbol)")
    Optional<MonthlyStat> findCryptoStatWithOldestCryptoPriceBySymbol(@Param("symbol") String symbol);

    @Query("SELECT ms FROM MonthlyStat ms JOIN ms.newestCryptoPrice cp " +
            "WHERE ms.symbol = :symbol AND cp.timestamp = " +
            "(SELECT MAX(cpInner.timestamp) FROM MonthlyStat msInner JOIN msInner.newestCryptoPrice cpInner " +
            "WHERE msInner.symbol = :symbol)")
    Optional<MonthlyStat> findCryptoStatWithNewestCryptoPriceBySymbol(@Param("symbol") String symbol);

}
