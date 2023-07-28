package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.request.DepositRequest;
import com.example.bankingApp.account.model.request.TransferRequest;
import com.example.bankingApp.account.model.request.WithdrawRequest;
import com.example.bankingApp.account.model.response.AccountList;
import com.example.bankingApp.account.model.response.TransferResponse;
import com.example.bankingApp.account.model.response.WithdrawResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    ResponseEntity<String> createAccount(Account account);

    ResponseEntity<List<AccountList>> getAccountsByUserId();

    ResponseEntity<WithdrawResponse> deposit(DepositRequest depositRequest);

    ResponseEntity<TransferResponse> transfer(TransferRequest transferRequest);

    ResponseEntity<String> depositWithCurrency(Long accountId, String amount, String currency);

    ResponseEntity<String> transferWithCurrency(Long sourceAccountId, Long targetAccountId, String amount, String currency);

    ResponseEntity<WithdrawResponse> withdraw(WithdrawRequest withdrawRequest);
}