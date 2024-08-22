package com.temirlan.transactionservice.controllers;


import com.temirlan.transactionservice.entities.TransactionEntity;
import com.temirlan.transactionservice.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/create")
    public TransactionEntity createTransaction(@RequestBody TransactionEntity transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/exchange-rate/{currency}")
    public BigDecimal getExchangeRate(@PathVariable String currency) {
        return transactionService.getExchangeRate(currency);
    }

    @GetMapping("/{id}")
    public TransactionEntity getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/all")
    public List<TransactionEntity> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PatchMapping("/update/{id}")
    public TransactionEntity updateTransaction(@PathVariable Long id, @RequestBody TransactionEntity updatedTransaction) {
        return transactionService.updateTransaction(id, updatedTransaction);
    }

    @PatchMapping("/set-monthly-limit")
    public void setNewMonthlyLimit(@RequestParam BigDecimal newLimit) {
        transactionService.setNewMonthlyLimit(newLimit);
    }

    @GetMapping("/exceeding-limit")
    public List<TransactionEntity> getTransactionsExceedingLimit() {
        return transactionService.getTransactionsExceedingLimit();
    }

}
