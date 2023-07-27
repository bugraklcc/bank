package com.example.bankingApp.auth.domain.error;

import com.example.bankingApp.common.ErrorMessage;

public enum AuthErrorResponseType implements ErrorMessage {
    AUTH_NOT_FOUND("Hesap bakiyesi yetersiz.");

    private String message;

    AuthErrorResponseType(String s) {
        this.message = s;
    }

    @Override
    public String message() {
        return message;
    }
}
