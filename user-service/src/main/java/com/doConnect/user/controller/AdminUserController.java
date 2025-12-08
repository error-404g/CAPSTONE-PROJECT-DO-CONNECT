package com.doConnect.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doConnect.user.entity.User;
import com.doConnect.user.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
	
	@Autowired
    private UserRepository userRepository;

    
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> getAdmins() {
        return userRepository.findByRole("ROLE_ADMIN");
    }

    @GetMapping("/email/{email}")  // ← THIS PATH
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public User createAdmin(@RequestBody User dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setRole("ROLE_ADMIN");
        dto.setIsActive(true);
        return userRepository.save(dto);
    }

    @PutMapping("/{id}")
    public User updateAdmin(@PathVariable Long id, @RequestBody User dto) {
        User admin = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userRepository.save(admin);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
