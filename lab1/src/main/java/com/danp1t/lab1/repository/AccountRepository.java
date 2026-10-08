package com.danp1t.lab1.repository;

import com.danp1t.lab1.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
