package com.temirlan.transactionservice;


import com.temirlan.transactionservice.entities.ExchangeRateEntity;
import com.temirlan.transactionservice.repositories.ExchangeRateRepository;
import com.temirlan.transactionservice.services.CurrencyExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyExchangeServiceTests {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeRate() {
        // Arrange
        String currency = "USD";
        BigDecimal rate = BigDecimal.valueOf(1.13);
        ExchangeRateEntity exchangeRateEntity = ExchangeRateEntity.builder()
                .currency(currency)
                .rate(rate)
                .build();

        when(exchangeRateRepository.findByCurrency(currency)).thenReturn(Optional.of(exchangeRateEntity));

        // Act
        BigDecimal result = currencyExchangeService.getExchangeRate(currency);

        // Assert
        assertEquals(rate, result);
    }

    @Test
    void testGetExchangeRate_NotFound() {
        // Arrange
        String currency = "USD";

        when(exchangeRateRepository.findByCurrency(currency)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> currencyExchangeService.getExchangeRate(currency));
    }

}
