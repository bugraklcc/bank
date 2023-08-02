package com.example.bankingApp.account.service;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.account.model.request.*;
import com.example.bankingApp.account.model.response.*;
import com.example.bankingApp.account.repository.AccountRepository;
import com.example.bankingApp.account.service.exchange.ExchangeService;
import com.example.bankingApp.account.service.pdf.PdfService;
import com.example.bankingApp.auth.domain.UserEntity;
import com.example.bankingApp.auth.domain.error.AuthErrorResponse;
import com.example.bankingApp.auth.domain.error.AuthErrorResponseType;
import com.example.bankingApp.auth.repository.UserRepository;
import com.example.bankingApp.auth.utils.AuthenticationUtils;
import com.example.bankingApp.transaction.model.Transaction;
import com.example.bankingApp.transaction.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final PdfService pdfService;

    @Override
    public ResponseEntity<String> createAccount(Account account) {
        UserEntity userInfo = userRepository.findById(authenticationUtils.getCurrentUserId()).orElse(null);

        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
        }

        account.setUser(userInfo); // Kullanıcı nesnesini hesap nesnesine bağlamayı burada yapın
        accountRepository.save(account);
        return ResponseEntity.ok("Hesap başarıyla oluşturuldu.");
    }

    @Override
    public ResponseEntity<List<AccountList>> getAccountsByUserId() {

        UserEntity user = userRepository.findById(authenticationUtils.getCurrentUserId())
                .orElseThrow(() -> new AuthErrorResponse(AuthErrorResponseType.ACCOUNT_NOT_FOUND));
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
    public ResponseEntity<WithdrawResponse> deposit(DepositRequest depositRequest) {
        Long accountId = depositRequest.getAccountId();
        BigDecimal amount = new BigDecimal(String.valueOf(depositRequest.getAmount()));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AuthErrorResponse(AuthErrorResponseType.ACCOUNT_NOT_FOUND));

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
        String transactionType = "Para Yatırma";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String transactionDate = LocalDateTime.now().format(formatter);

        String pdfContent = "Yapılan İşlem: " + transactionType + "\n"
                + "İşlem Tarihi: " + transactionDate + "\n"
                + "Hesap No: " + response.getAccountId() + "\n"
                + "Kullanıcı Adı: " + response.getUsername() + "\n"
                + "E-Posta: " + response.getEmail() + "\n"
                + "Önceki Bakiye: " + response.getPreviousBalance() + "\n"
                + "Yeni Bakiye: " + response.getNewBalance() + "\n";
        String pdfFilePath = "pdf_output" + response.getAccountId() + "_deposit.pdf";

        String pdfCreationResult = pdfService.createPdf(pdfContent, pdfFilePath);
        System.out.println(pdfCreationResult);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<WithdrawResponse> withdraw(WithdrawRequest withdrawRequest) {
        Long accountId = withdrawRequest.getAccountId();
        BigDecimal amount = new BigDecimal(String.valueOf(withdrawRequest.getAmount()));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AuthErrorResponse(AuthErrorResponseType.ACCOUNT_NOT_FOUND));

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
            String transactionType = "Para Çekme";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String transactionDate = LocalDateTime.now().format(formatter);

            String pdfContent = "Yapılan İşlem: " + transactionType + "\n"
                    + "İşlem Tarihi: " + transactionDate + "\n"
                    + "Hesap No: " + response.getAccountId() + "\n"
                    + "Kullanıcı Adı: " + response.getUsername() + "\n"
                    + "E-Posta: " + response.getEmail() + "\n"
                    + "Önceki Bakiye: " + response.getPreviousBalance() + "\n"
                    + "Yeni Bakiye: " + response.getNewBalance() + "\n";
            String pdfFilePath = "pdf_output" + response.getAccountId() + "_withdraw.pdf";

            String pdfCreationResult = pdfService.createPdf(pdfContent, pdfFilePath);
            System.out.println(pdfCreationResult);
        } else {
            // Yeterli bakiye yok, çekme işlemi gerçekleştirilemedi
            response.setNewBalance(currentBalance.toString()); // Yeni bakiyeyi önceki bakiye ile aynı yapalım
            throw new AuthErrorResponse(AuthErrorResponseType.INSUFFICIENT_BALANCE_WITHDRAW);

        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransferResponse> transfer(TransferRequest transferRequest) {
        Long sourceAccountId = transferRequest.getSourceAccountId();
        Long targetAccountId = transferRequest.getTargetAccountId();
        BigDecimal amount = new BigDecimal(String.valueOf(transferRequest.getAmount()));
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new AuthErrorResponse(AuthErrorResponseType.SOURCE_ACCOUNT_NOT_FOUND));

        Account targetAccount = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new AuthErrorResponse(AuthErrorResponseType.TARGET_ACCOUNT_NOT_FOUND));

        BigDecimal sourceBalance = sourceAccount.getBalance();
        BigDecimal targetBalance = targetAccount.getBalance();
        BigDecimal newSourceBalance = sourceBalance.subtract(amount);
        BigDecimal newTargetBalance = targetBalance.add(amount);

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

            String transactionType = "Para Transferi";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String transactionDate = LocalDateTime.now().format(formatter);
            String pdfContent = "Yapılan İşlem: " + transactionType + "\n"
                    + "İşlem Tarihi: " + transactionDate + "\n"
                    + "Gönderen Hesap No: " + sourceAccountId + "\n"
                    + "Gönderen Kullanıcı Adı: " + sourceAccount.getUser().getUsername() + "\n"
                    + "Gönderen E-Posta: " + sourceAccount.getUser().getEmail() + "\n"
                    + "Gönderen Hesap Önceki Bakiye: " + sourceBalance + "\n"
                    + "Gönderen Hesap Yeni Bakiye: " + newSourceBalance + "\n"
                    + "Transfer Edilen Miktar: " + amount + "\n"
                    + "Alıcı Hesap No: " + targetAccountId + "\n"
                    + "Alıcı Kullanıcı Adı: " + targetAccount.getUser().getUsername() + "\n"
                    + "Alıcı E-Posta: " + targetAccount.getUser().getEmail() + "\n"
                    + "Alıcı Hesap Önceki Bakiye: " + targetBalance + "\n"
                    + "Alıcı Hesap Yeni Bakiye: " + newTargetBalance + "\n";

            String pdfFilePath = "pdf_output" + sourceAccountId + "_transfer.pdf";

            String pdfCreationResult = pdfService.createPdf(pdfContent, pdfFilePath);
            System.out.println(pdfCreationResult);

            TransferResponse response = TransferResponse.builder()
                    .sourceAccountId(sourceAccountId)
                    .targetAccountId(targetAccountId)
                    .sourceBalance(sourceBalance.toString())
                    .targetBalance(targetBalance.toString())
                    .build();

            return ResponseEntity.ok(response);
        } else {
            // Yetersiz bakiye, transfer işlemi gerçekleştirilemedi
            throw new AuthErrorResponse(AuthErrorResponseType.INSUFFICIENT_BALANCE);
        }
    }

    @Override
    public ResponseEntity<DepositWithResponse> depositWithCurrency(DepositRequest depositRequest) {
        Long accountId = depositRequest.getAccountId();
        String amount = depositRequest.getAmount();
        String currency = depositRequest.getCurrency();
        BigDecimal amountValue = new BigDecimal(amount);

        // Hesabı alın
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        // Hesap para birimiyle yatırılan para birimi uyuşuyor mu kontrol edin
        if (!account.getCurrency().equalsIgnoreCase(currency)) {
            // Para birimi uyuşmazlığı, döviz kuru ile dönüşüm yapın
            BigDecimal convertedAmount = exchangeService.convertAmount(currency, account.getCurrency(), amountValue);
            amountValue = convertedAmount;
        }

        // Yatırılan miktarı hesap para birimine çevirin
        BigDecimal newBalance = account.getBalance().add(amountValue);
        account.setBalance(newBalance);
        accountRepository.save(account);

        // İşlem detaylarını kaydedin
        Transaction transaction = new Transaction();
        transaction.setSender(null); // Para yatırma işleminde gönderen yok
        transaction.setReceiver(account);
        transaction.setAmount(amountValue);
        transaction.setCurrency(account.getCurrency());
        transaction.setUser(account.getUser());
        transactionRepository.save(transaction);

        // Yanıt nesnesini hazırlayın
        DepositWithResponse response = DepositWithResponse.builder()
                .status("success")
                .message("Para yatırma işlemi başarılı.")
                .username(account.getUser().getUsername())
                .accountId(accountId)
                .email(account.getUser().getEmail())
                .previousBalance(account.getBalance().subtract(amountValue).setScale(2, RoundingMode.HALF_EVEN).toString())
                .newBalance(newBalance.setScale(2, RoundingMode.HALF_EVEN).toString())
                .currency(account.getCurrency())
                .build();
        String transactionType = "Para Yatırma";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String transactionDate = LocalDateTime.now().format(formatter);

        String pdfContent = "Yapılan İşlem: " + transactionType + "\n"
                + "İşlem Tarihi: " + transactionDate + "\n"
                + "Hesap No: " + response.getAccountId() + "\n"
                + "Kullanıcı Adı: " + response.getUsername() + "\n"
                + "E-Posta: " + response.getEmail() + "\n"
                + "Önceki Bakiye: " + response.getPreviousBalance() + "\n"
                + "Yeni Bakiye: " + response.getNewBalance() + "\n";
        String pdfFilePath = "pdf_output" + response.getAccountId() + "_depositWith.pdf";

        String pdfCreationResult = pdfService.createPdf(pdfContent, pdfFilePath);
        System.out.println(pdfCreationResult);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<TransferWithResponse> transferWithCurrency(TransferWithRequest transferWithRequest) {
        BigDecimal amountValue = new BigDecimal(transferWithRequest.getAmount());

        // Fetch the source and target accounts
        Account sourceAccount = accountRepository.findById(transferWithRequest.getSourceAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account targetAccount = accountRepository.findById(transferWithRequest.getTargetAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Target account not found"));

        // Check if both accounts have the same currency or find an exchange rate
        BigDecimal exchangeRate = BigDecimal.ONE; // Default exchange rate for the same currency
        String transferCurrency = transferWithRequest.getCurrency(); // Transfer için kullanılacak para birimi
        if (!sourceAccount.getCurrency().equals(transferCurrency)) {
            exchangeRate = exchangeService.getExchangeRate(sourceAccount.getCurrency(), transferCurrency);
            if (exchangeRate == null) {
                TransferWithResponse response = TransferWithResponse.builder()
                        .status("error")
                        .message("Döviz kuru bulunamadı. Para transferi işlemi gerçekleştirilemedi.")
                        .build();
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Convert the amount to the target currency
        BigDecimal transferredAmount = amountValue.multiply(exchangeRate);

        // Check if the source account has sufficient balance
        BigDecimal sourceBalance = sourceAccount.getBalance();
        BigDecimal newSourceBalance = sourceBalance.subtract(amountValue);
        if (newSourceBalance.compareTo(BigDecimal.ZERO) >= 0) {
            // Update the source account balance
            sourceAccount.setBalance(newSourceBalance);
            accountRepository.save(sourceAccount);

            // Update the target account balance
            BigDecimal targetBalance = targetAccount.getBalance();
            BigDecimal newTargetBalance = targetBalance.add(transferredAmount);
            targetAccount.setBalance(newTargetBalance);
            accountRepository.save(targetAccount);

            // Store the transaction details
            Transaction transaction = new Transaction();
            transaction.setSender(sourceAccount);
            transaction.setReceiver(targetAccount);
            transaction.setAmount(transferredAmount);
            transaction.setCurrency(sourceAccount.getCurrency());
            transaction.setUser(sourceAccount.getUser());
            transaction.setCreatedAt(LocalDateTime.now()); // İşlem tarihini ekleyin
            transactionRepository.save(transaction);

            // Prepare the response object
            TransferWithResponse response = TransferWithResponse.builder()
                    .sourceAccountId(sourceAccount.getAccountId())
                    .senderUsername(sourceAccount.getUser().getUsername())
                    .senderEmail(sourceAccount.getUser().getEmail())
                    .sourceBalance(sourceBalance.toString())
                    .targetAccountId(targetAccount.getAccountId())
                    .receiverUsername(targetAccount.getUser().getUsername())
                    .receiverEmail(targetAccount.getUser().getEmail())
                    .targetBalance(targetBalance.toString())
                    .transferCurrency(transferCurrency)
                    .transferredAmount(transferredAmount.toString())
                    .build();
            String transactionType = "Para Transferi";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String transactionDate = LocalDateTime.now().format(formatter);
            String pdfContent = "Yapılan İşlem: " + transactionType + "\n"
                    + "İşlem Tarihi: " + transactionDate + "\n"
                    + "Gönderen Hesap No: " + response.getSourceAccountId() + "\n"
                    + "Gönderen Kullanıcı Adı: " + response.getSenderUsername() + "\n"
                    + "Gönderen E-Posta: " + response.getSenderEmail() + "\n"
                    + "Gönderen Hesap Önceki Bakiye: " + response.getSourceBalance() + "\n"
                    + "Gönderen Hesap Yeni Bakiye: " + newSourceBalance + "\n"
                    + "Transfer Edilen Miktar: " + response.getTransferredAmount() + "\n"
                    + "Alıcı Hesap No: " + response.getTargetAccountId() + "\n"
                    + "Alıcı Kullanıcı Adı: " + response.getReceiverUsername() + "\n"
                    + "Alıcı E-Posta: " + response.getReceiverEmail() + "\n"
                    + "Alıcı Hesap Önceki Bakiye: " + response.getTargetBalance() + "\n"
                    + "Alıcı Hesap Yeni Bakiye: " + newTargetBalance + "\n";

            String pdfFilePath = "pdf_output" + response.getSourceAccountId() + "_transferWith.pdf";

            String pdfCreationResult = pdfService.createPdf(pdfContent, pdfFilePath);
            System.out.println(pdfCreationResult);
            return ResponseEntity.ok(response);
        } else {
            TransferWithResponse response = TransferWithResponse.builder()
                    .status("error")
                    .message("Kaynak hesabın bakiyesi yetersiz. Transfer işlemi gerçekleştirilemedi.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
