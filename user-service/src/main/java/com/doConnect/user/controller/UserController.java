package com.doConnect.user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doConnect.user.dto.UserRequest;
import com.doConnect.user.dto.UserResponse;
import com.doConnect.user.entity.User;
import com.doConnect.user.repository.UserRepository;
import com.doConnect.user.service.QuestionService;
import com.doConnect.user.service.UserService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired 
    private UserRepository userRepository;
    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsersForAdmin() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
  
 

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
            .map(user -> ResponseEntity.ok(user))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Login user.
     * URL: POST /api/users/login
     * RequestBody: UserRequest JSON containing email and password.
     * Response: UserResponse JSON with user details if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
        UserResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * Logout user by ID.
     * URL: POST /api/users/logout/{userId}
     * Response: Success message.
     */
    @PostMapping("/logout/{userId}")
    public ResponseEntity<String> logout(@PathVariable Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok("Logged out successfully");
    }
    
    @PostMapping("/admin/users/{id}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.ok("User blocked: " + id);
    }

    @PostMapping("/admin/users/{id}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.ok("User unblocked: " + id);
    }

}
