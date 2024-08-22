package com.temirlan.transactionservice.repositories;

import com.temirlan.transactionservice.entities.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, Long> {
    Optional<ExchangeRateEntity> findByCurrency(String currency);

    Optional<ExchangeRateEntity> findByCurrencyAndTimestampAfter(String currency, Instant timestamp);

    Optional<ExchangeRateEntity> findTopByCurrencyOrderByTimestampDesc(String currency);
}