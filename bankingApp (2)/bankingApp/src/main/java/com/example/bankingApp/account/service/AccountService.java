package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.response.AccountList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
  ResponseEntity<String> createAccount(Account account);
  ResponseEntity<List<AccountList>> getAccountsByUserId();
}
