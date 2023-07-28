package com.example.bankingApp.auth.domain.error;

import com.example.bankingApp.common.ErrorMessage;

public enum AuthErrorResponseType implements ErrorMessage {
    AUTH_NOT_FOUND("Hesap bakiyesi yetersiz."),
    TEST_EXCEPTION_NOT_FOUND("test exception found"),
    SOURCE_ACCOUNT_NOT_FOUND("Kaynak hesap bulunamadı."),
    TARGET_ACCOUNT_NOT_FOUND("Hedef hesap bulunamadı."),
    INSUFFICIENT_BALANCE("Yetersiz bakiye, transfer işlemi gerçekleştirilemedi."),
    ACCOUNT_NOT_FOUND("Hesap bulunamadı."),
    INSUFFICIENT_BALANCE_WITHDRAW("Yeterli bakiye yok, çekme işlemi gerçekleştirilemedi.");
    private String message;

    AuthErrorResponseType(String s) {
        this.message = s;
    }

    @Override
    public String message() {
        return message;
    }
}
