package com.example.bankingApp.transaction.service;

import com.example.bankingApp.transaction.model.Transaction;
import com.example.bankingApp.transaction.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findBySenderAccountIdOrReceiverAccountId(accountId, accountId);
    }
}
