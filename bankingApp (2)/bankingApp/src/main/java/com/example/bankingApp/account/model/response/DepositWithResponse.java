package com.example.bankingApp.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositWithResponse {
        private String status;
        private String message;
        private String username;
        private Long accountId;
        private String email;
        private String previousBalance;
        private String newBalance;
        private String currency;
}
