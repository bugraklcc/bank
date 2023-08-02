package com.example.bankingApp.account.model.response;

import lombok.*;

@Getter
@Setter
@Builder
public class TransferWithResponse {
    private Long sourceAccountId;
    private String senderUsername;
    private String senderEmail;
    private Long targetAccountId;
    private String receiverUsername;
    private String receiverEmail;
    private String transferCurrency;
    private String transferredAmount;
    private String sourceBalance;
    private String targetBalance;
    private String status;
    private String message;
}

