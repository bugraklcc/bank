package com.example.bankingApp.account.model.request;

import lombok.Data;

@Data
public class DepositRequest {

        private Long accountId;
        private String amount;
        private String currency;

        public String getCurrency() {
                return currency;
        }

        public void setCurrency(String currency) {
                this.currency = currency;
        }
}
