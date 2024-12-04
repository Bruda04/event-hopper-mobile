package com.ftn.eventhopper.models;

import java.time.LocalDateTime;

public class Notification {
    private String title;
    private String text;
    private LocalDateTime timestamp;

    public Notification(String title, String text, LocalDateTime timestamp) {
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
