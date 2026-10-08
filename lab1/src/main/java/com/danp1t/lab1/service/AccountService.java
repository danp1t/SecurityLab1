package com.danp1t.lab1.service;

import com.danp1t.lab1.model.Account;
import com.danp1t.lab1.repository.AccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String login) throws UsernameNotFoundException {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found: " + login));

        return User.builder()
                .username(account.getLogin())
                .password(account.getHashPassword())
                .roles(account.getRole())
                .build();
    }

    public Account saveAccount(Account account) {
        account.setHashPassword(passwordEncoder.encode(account.getHashPassword()));
        return accountRepository.save(account);
    }

    public Account findByLogin(String login) {
        return accountRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Account not found: " + login));
    }
}