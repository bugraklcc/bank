package com.example.bankingApp.account.controller;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.response.AccountList;
import com.example.bankingApp.account.model.response.DepositRequest;
import com.example.bankingApp.account.model.response.TransferRequest;
import com.example.bankingApp.account.model.response.WithdrawRequest;
import com.example.bankingApp.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/list")
    public ResponseEntity<List<AccountList>> getAccountsByUserId() {
        return accountService.getAccountsByUserId();
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest depositRequest) {
        Long accountId = depositRequest.getAccountId();
        BigDecimal amount = new BigDecimal(depositRequest.getAmount());
        return accountService.deposit(accountId, amount);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        Long accountId = withdrawRequest.getAccountId();
        BigDecimal amount = new BigDecimal(withdrawRequest.getAmount());
        return accountService.withdraw(accountId, amount);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest) {
        Long sourceAccountId = transferRequest.getSourceAccountId();
        Long targetAccountId = transferRequest.getTargetAccountId();
        BigDecimal amount = new BigDecimal(transferRequest.getAmount());
        return accountService.transfer(sourceAccountId, targetAccountId, amount);
    }
}



