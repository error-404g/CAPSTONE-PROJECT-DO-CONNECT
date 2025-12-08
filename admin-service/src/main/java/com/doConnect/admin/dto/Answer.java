package com.doConnect.admin.dto;

import java.time.LocalDateTime;

public class Answer {
    private Long id;
    private String content, userEmail;
    private Long questionId;
    private LocalDateTime createdAt;
    private boolean approved;
    
    public Answer() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
}
