package com.doConnect.user.dto;

public class AnswerRequest {
    private String content;
    
    // Constructors
    public AnswerRequest() {}
    
    public AnswerRequest(String content) {
        this.content = content;
    }
    
    // Getters/Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
