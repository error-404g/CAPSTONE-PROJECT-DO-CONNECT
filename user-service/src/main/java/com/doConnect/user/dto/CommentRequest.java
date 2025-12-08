package com.doConnect.user.dto;

public class CommentRequest {
    private String userEmail;
    private String text;

    // Default constructor
    public CommentRequest() {}

    // Constructor with parameters
    public CommentRequest(String userEmail, String text) {
        this.userEmail = userEmail;
        this.text = text;
    }

    // Getters and setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
