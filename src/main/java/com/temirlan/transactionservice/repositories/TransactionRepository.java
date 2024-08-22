package com.temirlan.transactionservice.repositories;


import com.temirlan.transactionservice.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByLimitExceededTrue();

    List<TransactionEntity> findByDatetimeAfter(Instant datetime);
}
