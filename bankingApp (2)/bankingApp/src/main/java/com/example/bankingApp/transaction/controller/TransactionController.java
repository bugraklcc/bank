package com.example.bankingApp.transaction.controller;

import com.example.bankingApp.transaction.model.response.AccountList;
import com.example.bankingApp.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/list")
    public ResponseEntity<List<AccountList>> getAllAccountLists() {
        List<AccountList> accountLists = transactionService.getAllAccountLists();
        return ResponseEntity.ok(accountLists);
    }
}
