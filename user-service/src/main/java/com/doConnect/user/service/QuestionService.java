package com.doConnect.user.service;

import com.doConnect.user.dto.QuestionRequest;
import com.doConnect.user.dto.QuestionResponse;
import com.doConnect.user.entity.Question;


import java.util.List;
import java.util.Optional;

public interface QuestionService {
    QuestionResponse createQuestion(QuestionRequest request);
    List<QuestionResponse> getAllQuestions();
    QuestionResponse getQuestionById(Long id);
    List<QuestionResponse> searchQuestions(String query);
    
    List<QuestionResponse> getApprovedQuestions();
    List<QuestionResponse> getResolvedQuestions();
    
    void approveQuestion(Long id);
    void resolveQuestion(Long id);
    
    // Add getAllQuestionsForAdmin if needed
    List<Question> getAllQuestionsForAdmin();
    
    Optional<Question> findById(Long id);
    Question save(Question question);
    
    
}
