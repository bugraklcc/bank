package com.example.bankingApp.account.repository;


import com.example.bankingApp.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}




