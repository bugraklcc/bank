package com.example.bankingApp.transaction.repository;

import com.example.bankingApp.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccountIdOrReceiverAccountId(Long accountId, Long accountId1);
}
