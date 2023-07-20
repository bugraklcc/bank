package com.example.bankingApp.account.model.response;

import lombok.Data;

@Data
public class TransferRequest {


        private Long sourceAccountId;
        private Long targetAccountId;
        private String amount;

}
