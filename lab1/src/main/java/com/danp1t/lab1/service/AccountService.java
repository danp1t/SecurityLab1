package com.danp1t.lab1.service;

import com.danp1t.lab1.model.Account;
import com.danp1t.lab1.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public String getHashPassword(String password){
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(
                16,
                32,
                1,
                65536,
                3
        );

        return passwordEncoder.encode(password);
    }

    public Account saveAccount(Account account){
        return accountRepository.save(account);
    }
}
