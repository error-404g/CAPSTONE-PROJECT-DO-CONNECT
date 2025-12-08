package com.doConnect.user.service;

import com.doConnect.user.dto.UserRequest;
import com.doConnect.user.dto.UserResponse;
import com.doConnect.user.entity.User;
import com.doConnect.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public List<User> getAllUsers() {
        System.out.println("=== UserService.getAllUsers() CALLED ===");
        List<User> users = userRepository.findAll();
        System.out.println("=== UserService found " + users.size() + " users ===");
        return users;
    }

    
    @Override
    public void blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setIsActive(false);  
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setIsActive(true);  
        userRepository.save(user);
    }
    

    @Override
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setIsActive(true);
        user = userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.isActive());
    }

    @Override
    public UserResponse login(String email, String password) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }
        User user = optUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        // Check correct field
        if (!user.isActive()) {  
            throw new RuntimeException("User is blocked by admin");
        }
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.isActive());
    }

    	
    @Override
    public void logout(Long userId) {
        // Custom logout logic (optional)
        // E.g. invalidate session, revoke tokens, audit logout, etc.
    }
}
