package com.example.bankingApp.auth.domain.error;

public class AuthErrorResponse extends RuntimeException {

    public AuthErrorResponse(AuthErrorResponseType authErrorResponseType) {
        super(authErrorResponseType.message());
    }
}