package com.example.bankingApp.transaction.service;

import com.example.bankingApp.transaction.model.Transaction;
import com.example.bankingApp.transaction.model.response.AccountList;
import com.example.bankingApp.transaction.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public List<AccountList> getAllAccountLists() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(this::convertTransactionToAccountList).collect(Collectors.toList());
    }

    private AccountList convertTransactionToAccountList(Transaction transaction) {
        String transactionType;

        if (transaction.getSender() != null && transaction.getReceiver() != null) {
            transactionType = "Transfer";
        } else if (transaction.getSender() != null) {
            transactionType = "Withdrawal";
        } else if (transaction.getReceiver() != null) {
            transactionType = "Deposit";
        } else {
            transactionType = "Unknown";
        }

        BigDecimal previousBalance;
        if (transaction.getSender() != null) {
            previousBalance = transaction.getSender().getBalance().add(transaction.getAmount());
        } else {
            previousBalance = transaction.getReceiver().getBalance().subtract(transaction.getAmount());
        }

        return AccountList.builder()
                .username(transaction.getSender() != null ? transaction.getSender().getUsername() : "-")
                .email(transaction.getSender() != null ? transaction.getSender().getEmail() : "-")
                .previousBalance(previousBalance.toString())
                .balance(transaction.getSender() != null ? transaction.getSender().getBalance().toString() : "-")
                .currency(transaction.getSender() != null ? transaction.getSender().getCurrency() : "-")
                .createdAt(transaction.getSender() != null ? transaction.getSender().getCreatedAt() : null)
                .accountID(transaction.getSender() != null ? transaction.getSender().getAccountId().toString() : "-")
                .senderUsername(transaction.getSender() != null ? transaction.getSender().getUser().getUsername() : "-")
                .receiverUsername(transaction.getReceiver() != null ? transaction.getReceiver().getUser().getUsername() : "-")
                .transactionType(transactionType)
                .transactionDate(transaction.getCreatedAt())
                .transactionAmount(transaction.getAmount().toString())
                .build();
    }

}
