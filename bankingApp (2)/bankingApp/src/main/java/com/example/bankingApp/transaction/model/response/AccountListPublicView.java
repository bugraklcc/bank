package com.example.bankingApp.transaction.model.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountListPublicView {
    private String transactionType;
    private LocalDateTime transactionDate;
    private String transactionAmount;
}

