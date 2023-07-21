package com.example.bankingApp.transaction.service;

import com.example.bankingApp.transaction.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactionsByAccountId(Long accountId);
}
