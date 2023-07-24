package com.example.bankingApp.transaction.repository;

import com.example.bankingApp.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
