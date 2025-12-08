package com.doConnect.user.service;

import java.util.List;

import com.doConnect.user.dto.UserRequest;
import com.doConnect.user.dto.UserResponse;
import com.doConnect.user.entity.User;

public interface UserService {
	
	
    UserResponse register(UserRequest userRequest);
    UserResponse login(String email, String password);
    void logout(Long userId);
    
    void blockUser(Long id);
    void unblockUser(Long id);
    
    List<User> getAllUsers();
}
