package com.example.bankingApp.transaction.service;

import com.example.bankingApp.transaction.model.response.AccountList;
import com.example.bankingApp.transaction.model.response.PublicAccountListResponse;

import java.util.List;

public interface TransactionService {
    List<AccountList> getAllAccountLists();

    PublicAccountListResponse getAllAccountListsPublicView();
}
