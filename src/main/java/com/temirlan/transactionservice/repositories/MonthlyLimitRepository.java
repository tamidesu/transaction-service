package com.temirlan.transactionservice.repositories;

import com.temirlan.transactionservice.entities.MonthlyLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface MonthlyLimitRepository extends JpaRepository<MonthlyLimitEntity, Long> {

    Optional<MonthlyLimitEntity> findTopByOrderByLimitSetDateDesc();

    Optional<MonthlyLimitEntity> findByLimitSetDateAfter(Instant limitSetDate);
}
