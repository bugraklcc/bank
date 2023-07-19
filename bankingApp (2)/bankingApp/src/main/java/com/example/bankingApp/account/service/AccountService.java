package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public void depositMoney(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            BigDecimal currentBalance = account.getBalance();
            BigDecimal newBalance = currentBalance.add(amount);
            account.setBalance(newBalance);
            accountRepository.save(account);
        }
    }

    public void withdrawMoney(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            BigDecimal currentBalance = account.getBalance();
            if (currentBalance.compareTo(amount) >= 0) {
                BigDecimal newBalance = currentBalance.subtract(amount);
                account.setBalance(newBalance);
                accountRepository.save(account);
            }
        }
    }

    public void transferMoney(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        Account sourceAccount = accountRepository.findById(sourceAccountId).orElse(null);
        Account targetAccount = accountRepository.findById(targetAccountId).orElse(null);

        if (sourceAccount != null && targetAccount != null) {
            BigDecimal sourceBalance = sourceAccount.getBalance();
            BigDecimal targetBalance = targetAccount.getBalance();

            if (sourceBalance.compareTo(amount) >= 0) {
                BigDecimal newSourceBalance = sourceBalance.subtract(amount);
                BigDecimal newTargetBalance = targetBalance.add(amount);

                sourceAccount.setBalance(newSourceBalance);
                targetAccount.setBalance(newTargetBalance);

                accountRepository.save(sourceAccount);
                accountRepository.save(targetAccount);
            }
        }
    }
}
