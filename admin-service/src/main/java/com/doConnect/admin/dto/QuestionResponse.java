package com.doConnect.admin.dto;

import java.time.LocalDateTime;

public class QuestionResponse {
    private Long id;
    private String title;
    private String content;
    private String userEmail;
    private LocalDateTime createdAt;
    private boolean approved;
    private boolean resolved;

    public QuestionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    
    public Boolean getApproved() { return approved; }
    public Boolean getResolved() { return resolved; }
}
