package com.temirlan.transactionservice;


import com.temirlan.transactionservice.entities.TransactionEntity;
import com.temirlan.transactionservice.repositories.TransactionRepository;
import com.temirlan.transactionservice.services.CurrencyExchangeService;
import com.temirlan.transactionservice.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionById_Found() {
        // Arrange
        Long transactionId = 1L;
        TransactionEntity transaction = TransactionEntity.builder()
                .id(transactionId)
                .accountFrom(123L)
                .accountTo(456L)
                .currencyShortname("USD")
                .sum(BigDecimal.valueOf(800))
                .expenseCategory("services")
                .datetime(Instant.now())
                .limitSum(BigDecimal.valueOf(1000))
                .limitDatetime(Instant.now())
                .limitCurrencyShortname("USD")
                .build();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Act
        TransactionEntity result = transactionService.getTransactionById(transactionId);

        // Assert
        assertEquals(transaction, result);
    }
}