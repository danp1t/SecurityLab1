package com.danp1t.lab1.dto;

import lombok.Getter;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

@Getter
public class ResponsePost {
    private final Long id;
    private final String title;
    private final String text;
    private final LocalDateTime createdAt;
    private final ResponseAccount owner;

    public ResponsePost(Long id, String title, String text, LocalDateTime createdAt, ResponseAccount owner) {
        this.id = id;
        this.title = HtmlUtils.htmlEscape(title);
        this.text = HtmlUtils.htmlEscape(text);
        this.createdAt = createdAt;
        this.owner = owner;
    }

}
