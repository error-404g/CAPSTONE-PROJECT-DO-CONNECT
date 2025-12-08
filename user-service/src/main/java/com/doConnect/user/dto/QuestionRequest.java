package com.doConnect.user.dto;

public class QuestionRequest {
    private String title;
    private String content;
    
    // Constructors
    public QuestionRequest() {}
    
    public QuestionRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    // Getters/Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
