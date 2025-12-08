package com.doConnect.user.service;

import com.doConnect.user.dto.QuestionRequest;
import com.doConnect.user.dto.QuestionResponse;
import com.doConnect.user.entity.Question;
import com.doConnect.user.repository.QuestionRepository;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Override
    public QuestionResponse createQuestion(QuestionRequest request) {
        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setUserEmail("current-user@gmail.com"); 
        question = questionRepository.save(question);
        
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setTitle(question.getTitle());
        response.setContent(question.getContent());
        response.setUserEmail(question.getUserEmail());
        return response;
    }
    
    @Override
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }
    
    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }
    @Override
    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public QuestionResponse getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }
    
   
    
    private QuestionResponse convertToResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setTitle(question.getTitle());
        response.setContent(question.getContent());
        response.setUserEmail(question.getUserEmail());
        response.setCreatedAt(question.getCreatedAt());
        response.setApproved(question.isApproved());
        response.setResolved(question.isResolved());
        return response;
    }

    public List<Question> searchByKeyword(String keyword) {
        return questionRepository
            .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<QuestionResponse> searchQuestions(String query) {
        // use title+content search
        return questionRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void approveQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found: " + id));
        question.setApproved(true);
        questionRepository.save(question);
    }

    @Override
    public void resolveQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found: " + id));
        question.setResolved(true);
        questionRepository.save(question);
    }
    
    @Override
    public List<QuestionResponse> getApprovedQuestions() {
        return questionRepository.findByApprovedTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponse> getResolvedQuestions() {
        return questionRepository.findByResolvedTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
 

    @Override
    public List<Question> getAllQuestionsForAdmin() {
        return questionRepository.findAll();
    }

}
