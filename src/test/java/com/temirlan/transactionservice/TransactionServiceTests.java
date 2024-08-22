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
    void testCreateTransaction_LimitExceeded() {
        // Arrange
        TransactionEntity transaction = TransactionEntity.builder()
                .sum(BigDecimal.valueOf(150))
                .limitSum(BigDecimal.valueOf(100))
                .currencyShortname("EUR")
                .build();

        when(currencyExchangeService.getExchangeRate("USD")).thenReturn(BigDecimal.valueOf(1.2));

        // Act
        TransactionEntity result = transactionService.createTransaction(transaction);

        // Assert
        assertEquals("limit_exceeded", result.getExpenseCategory());
        verify(transactionRepository, times(1)).save(transaction);
    }
}