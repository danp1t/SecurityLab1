package com.danp1t.lab1.dto;

import lombok.Getter;
import org.springframework.web.util.HtmlUtils;

@Getter
public class ResponseAccount {
    private final Long id;
    private final String login;

    public ResponseAccount(Long id, String login) {
        this.id = id;
        this.login = HtmlUtils.htmlEscape(login);
    }
}
