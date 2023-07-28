package com.example.bankingApp.account.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WithdrawResponse {

    private String username;
    private Long accountId;
    private String email;
    private String previousBalance;
    private String newBalance;
}
