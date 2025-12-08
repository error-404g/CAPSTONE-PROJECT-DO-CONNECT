package com.doConnect.admin.dto;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Long questionId;
    private String userEmail, text, content;
    private LocalDateTime createdAt;
    
    public Comment() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
