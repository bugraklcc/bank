package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.response.AccountList;
import com.example.bankingApp.account.model.response.TransferResponse;
import com.example.bankingApp.account.model.response.WithdrawResponse;
import com.example.bankingApp.account.repository.AccountRepository;
import com.example.bankingApp.account.service.exchange.ExchangeService;
import com.example.bankingApp.auth.domain.UserEntity;
import com.example.bankingApp.auth.repository.UserRepository;
import com.example.bankingApp.auth.utils.AuthenticationUtils;
import com.example.bankingApp.transaction.model.Transaction;
import com.example.bankingApp.transaction.repository.TransactionRepository;
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

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuthenticationUtils authenticationUtils;
    private final ExchangeService exchangeService;
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
                    .accountId(account.getAccountId().toString())
                    .balance(account.getBalance().toString())
                    .currency(account.getCurrency())
                    .createdAt(account.getCreatedAt())
                    .build();
            accountLists.add(accountList);
        });
        return ResponseEntity.ok(accountLists);
    }

    @Override
    public ResponseEntity<WithdrawResponse> deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Transaction kaydını yapalım
        Transaction transaction = new Transaction();
        transaction.setSender(null); // Para yatırma işleminde gönderen yok
        transaction.setReceiver(account);
        transaction.setAmount(amount);
        transaction.setCurrency(account.getCurrency());
        transaction.setUser(account.getUser());
        transactionRepository.save(transaction);

        WithdrawResponse response = WithdrawResponse.builder()
                .accountId(accountId)
                .username(account.getUser().getUsername())
                .email(account.getUser().getEmail())
                .previousBalance(currentBalance.toString())
                .newBalance(newBalance.toString())
                .build();

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<WithdrawResponse> withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.subtract(amount);
        String username = account.getUser().getUsername();
        String email = account.getUser().getEmail();

        WithdrawResponse response = WithdrawResponse.builder()
                .accountId(accountId)
                .username(username)
                .email(email)
                .previousBalance(currentBalance.toString()) // Önceki bakiyeyi set edelim
                .build();

        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            // Yeterli bakiye var, çekme işlemi gerçekleştirelim
            account.setBalance(newBalance);
            accountRepository.save(account);
            Transaction transaction = new Transaction();
            transaction.setSender(account);
            transaction.setReceiver(null);
            transaction.setAmount(amount.negate());
            transaction.setCurrency(account.getCurrency());
            transaction.setUser(account.getUser());
            transactionRepository.save(transaction);

            response.setNewBalance(newBalance.toString()); // Yeni bakiyeyi set edelim
        } else {
            // Yeterli bakiye yok, çekme işlemi gerçekleştirilemedi
            response.setNewBalance(currentBalance.toString()); // Yeni bakiyeyi önceki bakiye ile aynı yapalım
            response.setErrorMessage("Çekmek için yeterli bakiye yok.");
        }

        return ResponseEntity.ok(response);
    }





    @Override
    public ResponseEntity<TransferResponse> transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {

        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Kaynak hesap bulunamadı"));

        Account targetAccount = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Hedef hesap bulunamadı"));

        BigDecimal sourceBalance = sourceAccount.getBalance();
        BigDecimal targetBalance = targetAccount.getBalance();
        BigDecimal newSourceBalance = sourceBalance.subtract(amount);
        BigDecimal newTargetBalance = targetBalance.add(amount);

        TransferResponse response = TransferResponse.builder()
                .sourceAccountId(sourceAccountId)
                .targetAccountId(targetAccountId)
                .sourceBalance(sourceBalance.toString())
                .targetBalance(targetBalance.toString())
                .build();

        if (newSourceBalance.compareTo(BigDecimal.ZERO) >= 0) {

            sourceAccount.setBalance(newSourceBalance);
            targetAccount.setBalance(newTargetBalance);
            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);
            Transaction transaction = new Transaction();
            transaction.setSender(sourceAccount);
            transaction.setReceiver(targetAccount);
            transaction.setAmount(amount);
            transaction.setCurrency(sourceAccount.getCurrency());
            transaction.setUser(sourceAccount.getUser());
            transactionRepository.save(transaction);

            response.setSourceBalance(newSourceBalance.toString()); // Kaynak hesap yeni bakiyesi
            response.setTargetBalance(newTargetBalance.toString()); // Hedef hesap yeni bakiyesi
        } else {
            // Yetersiz bakiye, transfer işlemi gerçekleştirilemedi
            response.setErrorMessage("Kaynak hesabın bakiyesi yetersiz. Transfer işlemi gerçekleştirilemedi.");
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> depositWithCurrency(Long accountId, String amount, String currency) {
        // Burada döviz kuru servisine çağrı yaparak güncel döviz kurlarını alabilir ve para yüklemeyi gerçekleştirebilirsiniz.
        // Döviz kurlarını kullanarak para yükleme işlemini implemente edin.
        BigDecimal amountValue = new BigDecimal(amount);
        // Döviz kurlarını exchangeService üzerinden alalım
        BigDecimal exchangeRate = exchangeService.getExchangeRate(currency, "TRY");
        // İlk parametre verilen para birimine, ikinci parametre ise çevrilmek istenen para birimine karşılık gelen kuru döndürecektir.
        if (exchangeRate == null) {
            return ResponseEntity.badRequest().body("Döviz kuru bulunamadı. Para yükleme işlemi gerçekleştirilemedi.");
        }

        BigDecimal newBalance = amountValue.multiply(exchangeRate);
        // Hesap üzerinde güncelleme yapalım
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        BigDecimal currentBalance = account.getBalance();
        BigDecimal updatedBalance = currentBalance.add(newBalance);
        account.setBalance(updatedBalance);
        accountRepository.save(account);
        // Transaction kaydını yapalım
        Transaction transaction = new Transaction();
        transaction.setSender(null); // Para yükleme işleminde gönderen yok
        transaction.setReceiver(account);
        transaction.setAmount(newBalance);
        transaction.setCurrency(account.getCurrency());
        transaction.setUser(account.getUser());
        transactionRepository.save(transaction);
        return ResponseEntity.ok("Para yükleme işlemi başarılı. Yeni bakiye: " + updatedBalance);
    }

    @Override
    public ResponseEntity<String> transferWithCurrency(Long sourceAccountId, Long targetAccountId, String amount, String currency) {

        BigDecimal amountValue = new BigDecimal(amount);
        BigDecimal exchangeRate = exchangeService.getExchangeRate(currency, "TRY");
        if (exchangeRate == null) {
            return ResponseEntity.badRequest().body("Döviz kuru bulunamadı. Para transferi işlemi gerçekleştirilemedi.");
        }

        BigDecimal transferredAmount = amountValue.multiply(exchangeRate);
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        BigDecimal sourceBalance = sourceAccount.getBalance();
        BigDecimal newSourceBalance = sourceBalance.subtract(transferredAmount);

        if (newSourceBalance.compareTo(BigDecimal.ZERO) >= 0) {

            sourceAccount.setBalance(newSourceBalance);
            accountRepository.save(sourceAccount);
            Account targetAccount = accountRepository.findById(targetAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("Target account not found"));
            BigDecimal targetBalance = targetAccount.getBalance();
            BigDecimal newTargetBalance = targetBalance.add(transferredAmount);
            targetAccount.setBalance(newTargetBalance);
            accountRepository.save(targetAccount);
            Transaction transaction = new Transaction();
            transaction.setSender(sourceAccount);
            transaction.setReceiver(targetAccount);
            transaction.setAmount(transferredAmount);
            transaction.setCurrency(sourceAccount.getCurrency());
            transaction.setUser(sourceAccount.getUser());
            transactionRepository.save(transaction);
            return ResponseEntity.ok("Para transferi işlemi başarılı. Kaynak hesap bakiyesi: " + newSourceBalance
                    + ", Hedef hesap bakiyesi: " + newTargetBalance);
        } else {
            return ResponseEntity.badRequest().body("Kaynak hesabın bakiyesi yetersiz. Transfer işlemi gerçekleştirilemedi.");
        }
    }



}
