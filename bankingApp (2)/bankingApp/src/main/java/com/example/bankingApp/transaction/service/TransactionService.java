package com.example.bankingApp.transaction.service;

import com.example.bankingApp.transaction.model.response.AccountList;

import java.util.List;

public interface TransactionService {
    List<AccountList> getAllAccountLists();
}
