package com.example.bankingApp.transaction.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AccountList {

    private String username;
    private String email;
    private String previousBalance;
    private String balance;
    private String currency;
    private LocalDateTime createdAt;
    private String accountID;

    private String senderUsername;
    private String receiverUsername;
    private String transactionType;
    private LocalDateTime transactionDate;
    private String transactionAmount;
}
