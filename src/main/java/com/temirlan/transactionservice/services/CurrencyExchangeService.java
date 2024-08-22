package com.temirlan.transactionservice.services;

import com.temirlan.transactionservice.entities.ExchangeRateEntity;
import com.temirlan.transactionservice.repositories.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@Service
public class CurrencyExchangeService {

    private final RestTemplate restTemplate;
    private final ExchangeRateRepository exchangeRateRepository;

    private static final String API_URL = "http://api.exchangeratesapi.io/v1/latest?access_key=3eeef9ee7a3e149c78d85ca6dff82655";

    @Autowired
    public CurrencyExchangeService(ExchangeRateRepository exchangeRateRepository) {
        this.restTemplate = new RestTemplate();
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public Map<String, BigDecimal> fetchExchangeRates() {

        Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
        Map<String, BigDecimal> rates = (Map<String, BigDecimal>) response.get("rates");

        return rates;
    }

    public void storeExchangeRates() {
        Map<String, BigDecimal> rates = fetchExchangeRates();
        Instant timestamp = Instant.now();

        for (Map.Entry<String, BigDecimal> entry : rates.entrySet()) {
            BigDecimal rateValue = new BigDecimal(((Number) entry.getValue()).doubleValue());

            ExchangeRateEntity exchangeRate = ExchangeRateEntity.builder()
                    .currency(entry.getKey())
                    .rate(rateValue)
                    .timestamp(timestamp)
                    .build();
            exchangeRateRepository.save(exchangeRate);
        }
    }

    public BigDecimal getExchangeRate(String currency) {
        LocalDate today = LocalDate.now();

        Instant todayStart = today.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Optional<ExchangeRateEntity> todayRate = exchangeRateRepository.findByCurrencyAndTimestampAfter(currency, todayStart);

        if (todayRate.isPresent()) {
            return todayRate.get().getRate();
        } else {
            return exchangeRateRepository.findTopByCurrencyOrderByTimestampDesc(currency)
                    .map(ExchangeRateEntity::getRate)
                    .orElseThrow(() -> new RuntimeException("Rate not found for currency: " + currency));
        }
    }

}