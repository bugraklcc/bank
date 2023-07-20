package com.example.bankingApp.auth.service;


import com.example.bankingApp.auth.domain.UserEntity;
import com.example.bankingApp.auth.model.request.AuthRequest;
import com.example.bankingApp.auth.model.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

  ResponseEntity<UserEntity> registerUser(UserEntity userEntity);

  ResponseEntity<AuthResponse> loginUser(AuthRequest authRequest);

  ResponseEntity<String> testLoggedUserName(Long userId);
}
