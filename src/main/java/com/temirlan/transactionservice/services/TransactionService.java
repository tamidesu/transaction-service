package com.temirlan.transactionservice.services;

import com.temirlan.transactionservice.entities.MonthlyLimitEntity;
import com.temirlan.transactionservice.entities.TransactionEntity;
import com.temirlan.transactionservice.repositories.MonthlyLimitRepository;
import com.temirlan.transactionservice.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CurrencyExchangeService currencyExchangeService;
    private final MonthlyLimitRepository monthlyLimitRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              CurrencyExchangeService currencyExchangeService,
                              MonthlyLimitRepository monthlyLimitRepository) {
        this.transactionRepository = transactionRepository;
        this.currencyExchangeService = currencyExchangeService;
        this.monthlyLimitRepository = monthlyLimitRepository;
    }


    // to create a transaction
    public TransactionEntity createTransaction(TransactionEntity transaction) {

        CheckForLimitExceeded(transaction);

        return transactionRepository.save(transaction);
    }

    // retrieve transactions that exceeded the limit
    public List<TransactionEntity> getTransactionsExceedingLimit() {
        return transactionRepository.findByLimitExceededTrue();
    }


    public BigDecimal getExchangeRate(String currency) {
        return currencyExchangeService.getExchangeRate(currency);
    }

    public BigDecimal getCurrentMonthlyLimit() {
        // Fetch the most recent monthly limit
        return monthlyLimitRepository.findTopByOrderByLimitSetDateDesc()
                .map(MonthlyLimitEntity::getLimitAmount)
                .orElseThrow(() -> new RuntimeException("No monthly limit found"));
    }

    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public TransactionEntity updateTransaction(Long id, TransactionEntity updatedTransaction) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setSum(updatedTransaction.getSum());
        transaction.setExpenseCategory(updatedTransaction.getExpenseCategory());
        transaction.setAccountFrom(updatedTransaction.getAccountFrom());
        transaction.setAccountTo(updatedTransaction.getAccountTo());
        transaction.setCurrencyShortname(updatedTransaction.getCurrencyShortname());
        transaction.setLimitSum(updatedTransaction.getLimitSum());
        transaction.setLimitDatetime(updatedTransaction.getLimitDatetime());
        transaction.setLimitCurrencyShortname(updatedTransaction.getLimitCurrencyShortname());

        CheckForLimitExceeded(transaction);

        return transactionRepository.save(transaction);
    }

    public void setNewMonthlyLimit(BigDecimal newLimit) {
        Instant now = Instant.now();

        MonthlyLimitEntity monthlyLimit = MonthlyLimitEntity.builder()
                .limitAmount(newLimit)
                .limitSetDate(now)
                .build();

        monthlyLimitRepository.save(monthlyLimit);
    }

    private void CheckForLimitExceeded(TransactionEntity transaction) {
        BigDecimal exchangeRateToUSD = currencyExchangeService.getExchangeRate(transaction.getCurrencyShortname());
        BigDecimal amountInUSD = transaction.getSum().multiply(exchangeRateToUSD);

        BigDecimal currentMonthlyLimit = getCurrentMonthlyLimit();

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        Instant startOfMonthInstant = startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
        List<TransactionEntity> transactionsThisMonth = transactionRepository.findByDatetimeAfter(startOfMonthInstant);

        BigDecimal totalSpentThisMonth = transactionsThisMonth.stream()
                .map(t -> t.getSum().multiply(currencyExchangeService.getExchangeRate(t.getCurrencyShortname())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        if (totalSpentThisMonth.add(amountInUSD).compareTo(currentMonthlyLimit) > 0) {
            transaction.setLimitExceeded(true);
            transaction.setExpenseCategory("limit_exceeded");
        } else {
            transaction.setLimitExceeded(false);
        }
    }


    public TransactionEntity getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

}