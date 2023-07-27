package com.example.bankingApp.account.model.request;

import lombok.Data;

@Data
public class TransferRequest {

        private Long sourceAccountId;
        private Long targetAccountId;
        private String amount;
        private String currency;

        public String getCurrency() {
                return currency;
        }

        public void setCurrency(String currency) {
                this.currency = currency;
        }
}
