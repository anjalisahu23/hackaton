package com.reputation.orchestration.domain;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class SlackMessage implements Serializable {
    private String username;
    private String text;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
