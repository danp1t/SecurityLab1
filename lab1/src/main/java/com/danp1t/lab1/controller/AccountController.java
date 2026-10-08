package com.danp1t.lab1.controller;

import com.danp1t.lab1.dto.RequestAccount;
import com.danp1t.lab1.dto.ResponseAccount;
import com.danp1t.lab1.model.Account;
import com.danp1t.lab1.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/api/create_user")
    public ResponseAccount createAccount(@RequestBody RequestAccount requestAccount) {
        Account account = new Account(requestAccount.getLogin(), requestAccount.getPassword(), "USER");
        account = accountService.saveAccount(account);
        return new ResponseAccount(account.getId(), account.getLogin());
    }
}
