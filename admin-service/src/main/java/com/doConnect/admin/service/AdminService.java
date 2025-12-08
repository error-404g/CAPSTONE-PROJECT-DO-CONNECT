package com.doConnect.admin.service;

import com.doConnect.admin.dto.AdminRequest;
import java.util.List;
import com.doConnect.admin.dto.QuestionDto;


public interface AdminService {

    void register(AdminRequest adminRequest);  

    //  methods
    void approveQuestion(Long id);
    void resolveQuestion(Long id);
    void getAllUsers();
    void blockUser(Long id);
    void unblockUser(Long id);
    
    List<QuestionDto> getAllQuestionsForAdmin();
}

