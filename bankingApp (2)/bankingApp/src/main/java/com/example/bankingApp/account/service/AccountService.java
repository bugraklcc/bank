package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.response.AccountList;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
  ResponseEntity<String> createAccount(Account account);
  ResponseEntity<List<AccountList>> getAccountsByUserId();
  ResponseEntity<String> deposit(Long accountId, BigDecimal amount);
  ResponseEntity<String> withdraw(Long accountId, BigDecimal amount);
  ResponseEntity<String> transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount);

}
