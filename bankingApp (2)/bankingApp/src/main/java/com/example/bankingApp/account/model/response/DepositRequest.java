package com.example.bankingApp.account.model.response;

import lombok.Data;


@Data

public class DepositRequest {

        private Long accountId;
        private String amount;

}
