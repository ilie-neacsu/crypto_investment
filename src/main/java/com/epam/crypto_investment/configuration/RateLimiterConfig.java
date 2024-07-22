package com.epam.crypto_investment.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    private final RateLimiterProperties rateLimiterProperties;

    public RateLimiterConfig(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    @Bean
    public Bucket bucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(rateLimiterProperties.getCapacity())
                .refillGreedy(
                        rateLimiterProperties.getRefillTokens(),
                        Duration.ofMinutes(rateLimiterProperties.getRefillDuration()))
                .build();

        return Bucket.builder().addLimit(limit).build();
    }
}
