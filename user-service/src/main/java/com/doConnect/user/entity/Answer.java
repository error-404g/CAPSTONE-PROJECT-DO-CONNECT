package com.doConnect.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    private String userEmail;
    private Long questionId;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean approved = false;
    private int likes = 0;
    
    // Constructors
    public Answer() {}
    
    public Answer(String content, String userEmail, Long questionId) {
        this.content = content;
        this.userEmail = userEmail;
        this.questionId = questionId;
    }
    
    // Getters/Setters
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
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
}
