package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.response.AccountList;
import com.example.bankingApp.account.repository.AccountRepository;
import com.example.bankingApp.auth.domain.UserEntity;
import com.example.bankingApp.auth.repository.UserRepository;
import com.example.bankingApp.auth.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AuthenticationUtils authenticationUtils;

    @Override
    public ResponseEntity<String> createAccount(Account account) {
        UserEntity userInfo = userRepository.findById(authenticationUtils.getCurrentUserId()).orElse(null);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
        }

        account.setUser(userInfo);
        accountRepository.save(account);

        return ResponseEntity.ok("Hesap başarıyla oluşturuldu.");
    }

    @Override
    public ResponseEntity<List<AccountList>> getAccountsByUserId() {

           UserEntity user=userRepository.findById(authenticationUtils.getCurrentUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // return accountRepository.findById(authenticationUtils.getCurrentUserId()).orElse(null);
        List<AccountList> accounts = new ArrayList<>();
        user.getAccounts().forEach(account -> {
            AccountList createdAccountResponse = AccountList.builder()
                    .build();
            accounts.add(createdAccountResponse);
        });
        return ResponseEntity.ok(accounts);

    }


}
