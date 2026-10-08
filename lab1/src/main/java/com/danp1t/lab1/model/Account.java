package com.danp1t.lab1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "hash_password", nullable = false)
    private String hashPassword;

    @Column(name = "role", nullable = false)
    private String role = "USER";

    public Account(String login, String hashPassword, String role) {
        this.login = login;
        this.hashPassword = hashPassword;
        this.role = role;
    }
}