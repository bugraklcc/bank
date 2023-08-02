package com.example.bankingApp.transaction.model;

import com.example.bankingApp.account.model.Account;
import com.example.bankingApp.auth.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "account_id")
    private Account receiver;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String sourceCurrency;
    private String targetCurrency;
    private LocalDateTime transactionDate;

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", user=" + user +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

}
