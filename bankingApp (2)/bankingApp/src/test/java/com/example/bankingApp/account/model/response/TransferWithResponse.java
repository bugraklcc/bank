package com.example.bankingApp.account.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransferWithResponse {

        private String status;
        private String message;
        private String sourceUsername;
        private Long sourceAccountId;
        private String sourceEmail;
        private String sourcePreviousBalance;
        private String sourceNewBalance;
        private String targetUsername;
        private Long targetAccountId;
        private String targetEmail;
        private String targetPreviousBalance;
        private String targetNewBalance;


}
