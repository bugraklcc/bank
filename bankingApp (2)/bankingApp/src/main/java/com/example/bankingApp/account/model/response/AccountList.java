package com.example.bankingApp.account.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class AccountList {

    private String username;
    private String email;
    private String balance;
    private String currency;
    private LocalDateTime createdAt;
    private String accountId;

}
