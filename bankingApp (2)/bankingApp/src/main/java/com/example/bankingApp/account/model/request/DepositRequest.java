package com.example.bankingApp.account.model.request;

import lombok.Data;


@Data

public class DepositRequest {

        private Long accountId;
        private String amount;

}
