package com.example.bankingApp.account.controller;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.request.DepositRequest;
import com.example.bankingApp.account.model.request.TransferRequest;
import com.example.bankingApp.account.model.request.TransferWithRequest;
import com.example.bankingApp.account.model.request.WithdrawRequest;
import com.example.bankingApp.account.model.response.*;
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

    @GetMapping("/list")
    public ResponseEntity<List<AccountList>> getAccountsByUserId() {
        return accountService.getAccountsByUserId();
    }

    @PostMapping("/deposit")
    public ResponseEntity<WithdrawResponse> deposit(@RequestBody DepositRequest depositRequest) {
        return accountService.deposit(depositRequest);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        return accountService.withdraw(withdrawRequest);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest transferRequest) {
        return accountService.transfer(transferRequest);
    }

    @PostMapping("/depositWithCurrency")
    public ResponseEntity<DepositWithResponse> depositWithCurrency(@RequestBody DepositRequest depositRequest) {

        return accountService.depositWithCurrency(depositRequest);
    }

    @PostMapping("/transferWithCurrency")
    public ResponseEntity<TransferWithResponse> transferWithCurrency(@RequestBody TransferWithRequest transferWithRequest) {

        return accountService.transferWithCurrency(transferWithRequest);
    }

}