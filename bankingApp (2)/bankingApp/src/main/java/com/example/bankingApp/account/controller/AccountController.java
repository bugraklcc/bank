package com.example.bankingApp.account.controller;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> depositMoney(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        accountService.depositMoney(accountId, amount);
        return ResponseEntity.ok("Para yükleme işlemi başarılı");
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdrawMoney(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        accountService.withdrawMoney(accountId, amount);
        return ResponseEntity.ok("Para çekme işlemi başarılı");
    }

    @PostMapping("/{sourceAccountId}/transfer")
    public ResponseEntity<String> transferMoney(@PathVariable Long sourceAccountId, @RequestParam Long targetAccountId, @RequestParam BigDecimal amount) {
        accountService.transferMoney(sourceAccountId, targetAccountId, amount);
        return ResponseEntity.ok("Para transferi işlemi başarılı");
    }
}
