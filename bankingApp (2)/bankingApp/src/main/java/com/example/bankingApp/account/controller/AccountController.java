package com.example.bankingApp.account.controller;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.response.AccountList;
import com.example.bankingApp.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/create")
  public ResponseEntity<String> createAccount(@RequestBody Account account) {
    return accountService.createAccount(account);

  }
  @GetMapping("/list/")
  public ResponseEntity<List<AccountList>> getAccountsByUserId() {
    return accountService.getAccountsByUserId();
  }



}
