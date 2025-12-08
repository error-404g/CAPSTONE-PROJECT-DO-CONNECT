package com.doConnect.admin.dto;

public class QuestionDto {
    private Long id;
    private String title;
    private String status;   // e.g. PENDING / APPROVED / RESOLVED

    public QuestionDto() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
