package com.example.bankingApp.account.model.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransferResponse {

    private Long sourceAccountId;
    private Long targetAccountId;
    private String sourceBalance;
    private String targetBalance;
}

