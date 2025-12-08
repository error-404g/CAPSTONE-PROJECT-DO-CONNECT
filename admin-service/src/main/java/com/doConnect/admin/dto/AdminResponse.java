package com.doConnect.admin.dto;

public class AdminResponse {
    private Long id;
    private String username;
    private String email;
    private boolean isActive;
    
    public AdminResponse() {}
    
    public AdminResponse(Long id, String username, String email, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
    }
    
    // GETTERS
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public boolean isActive() { return isActive; }
    
    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
}
