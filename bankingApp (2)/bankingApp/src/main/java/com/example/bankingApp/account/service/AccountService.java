package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.request.*;
import com.example.bankingApp.account.model.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    ResponseEntity<String> createAccount(Account account);

    ResponseEntity<List<AccountList>> getAccountsByUserId();

    ResponseEntity<WithdrawResponse> deposit(DepositRequest depositRequest);

    ResponseEntity<TransferResponse> transfer(TransferRequest transferRequest);

    ResponseEntity<DepositWithResponse> depositWithCurrency(DepositRequest depositRequest);

    ResponseEntity<WithdrawResponse> withdraw(WithdrawRequest withdrawRequest);

    ResponseEntity<TransferWithResponse> transferWithCurrency(TransferWithRequest transferWithRequest);
}