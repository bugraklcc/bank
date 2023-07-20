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

import java.math.BigDecimal;
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
        UserEntity user = userRepository.findById(authenticationUtils.getCurrentUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<AccountList> accountLists = new ArrayList<>();
        user.getAccounts().forEach(account -> {
            AccountList accountList = AccountList.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .accountID(account.getAccountId().toString())
                    .balance(account.getBalance().toString())
                    .currency(account.getCurrency())
                    .createdAt(account.getCreatedAt())
                    .build();
            accountLists.add(accountList);
        });

        return ResponseEntity.ok(accountLists);
    }

    @Override
    public ResponseEntity<String> deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        return ResponseEntity.ok("Para yükleme işlemi başarılı. Yeni bakiye: " + newBalance);
    }

    @Override
    public ResponseEntity<String> withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            account.setBalance(newBalance);
            accountRepository.save(account);
            return ResponseEntity.ok("Para çekme işlemi başarılı. Yeni bakiye: " + newBalance);
        } else {
            return ResponseEntity.badRequest().body("Bakiye yetersiz. Çekme işlemi gerçekleştirilemedi.");
        }
    }

    @Override
    public ResponseEntity<String> transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));

        Account targetAccount = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Target account not found"));

        BigDecimal sourceBalance = sourceAccount.getBalance();
        BigDecimal targetBalance = targetAccount.getBalance();

        BigDecimal newSourceBalance = sourceBalance.subtract(amount);
        BigDecimal newTargetBalance = targetBalance.add(amount);

        if (newSourceBalance.compareTo(BigDecimal.ZERO) >= 0) {
            sourceAccount.setBalance(newSourceBalance);
            targetAccount.setBalance(newTargetBalance);

            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);

            return ResponseEntity.ok("Para transferi işlemi başarılı. Kaynak hesap bakiyesi: " + newSourceBalance
                    + ", Hedef hesap bakiyesi: " + newTargetBalance);
        } else {
            return ResponseEntity.badRequest().body("Kaynak hesabın bakiyesi yetersiz. Transfer işlemi gerçekleştirilemedi.");
        }
    }


}
