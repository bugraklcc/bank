package com.example.bankingApp.account.model.request;

import lombok.Data;

@Data
public class TransferWithRequest {
    private Long sourceAccountId;
    private Long targetAccountId;
    private String amount;
    private String currency;
}

