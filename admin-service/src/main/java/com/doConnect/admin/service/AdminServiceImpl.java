package com.doConnect.admin.service;

import com.doConnect.admin.dto.AdminRequest;
import com.doConnect.admin.dto.QuestionDto;
import com.doConnect.admin.entity.Admin;
import com.doConnect.admin.repository.AdminRepository;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String USER_SERVICE_URL = "http://localhost:8081/api";

    @Override
    public void register(AdminRequest adminRequest) {
        Admin admin = new Admin();
        admin.setUsername(adminRequest.getUsername());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        admin.setActive(true); // if you have this field
        adminRepository.save(admin);
    }

    //  Question Actions
    @Override
    public void approveQuestion(Long id) {
        String url = USER_SERVICE_URL + "/questions/" + id + "/approve";
        restTemplate.postForObject(url, null, String.class);
    }

    @Override
    public void resolveQuestion(Long id) {
        restTemplate.postForObject(USER_SERVICE_URL + "/questions/" + id + "/resolve", null, String.class);
    }

    //  User Management
    @Override
    public void getAllUsers() {
        restTemplate.getForObject(USER_SERVICE_URL + "/users", String.class);
    }

    @Override
    public void blockUser(Long id) {
        restTemplate.postForObject(USER_SERVICE_URL + "/users/" + id + "/block", null, String.class);
    }

    @Override
    public void unblockUser(Long id) {
        restTemplate.postForObject(USER_SERVICE_URL + "/users/" + id + "/unblock", null, String.class);
    }
    @Override
    public List<QuestionDto> getAllQuestionsForAdmin() {
        QuestionDto[] arr =
            restTemplate.getForObject(USER_SERVICE_URL + "/questions", QuestionDto[].class);
        return Arrays.asList(arr);
    }
}
