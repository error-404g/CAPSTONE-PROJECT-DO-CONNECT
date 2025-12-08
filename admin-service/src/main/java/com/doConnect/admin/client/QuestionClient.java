package com.doConnect.admin.client;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.doConnect.admin.dto.QuestionResponse;
import com.doConnect.admin.dto.Question;

@FeignClient(name = "DOCONNECT-USER-SERVICE", contextId = "questionClient")
public interface QuestionClient {
	
	@GetMapping("/api/questions/{id}")
    Optional<Question> findById(@PathVariable Long id);
	    
	@PutMapping("/api/questions/{id}")
	Question updateQuestion(@PathVariable Long id, @RequestBody Question question);
	
	@GetMapping("/api/questions")
    List<QuestionResponse> getAllQuestions();  //  QuestionResponse DTO
    
    @GetMapping("/api/questions/{id}")
    QuestionResponse getQuestion(@PathVariable("id") Long id);  // DTO
    
    @GetMapping("/api/questions/{id}/likes/count")
    Long getLikeCount(@PathVariable("id") Long id); 
    
    @GetMapping("/api/questions/admin/all") 
    List<QuestionResponse> getAllQuestionsForAdmin();
    
    @PostMapping("/api/questions/{id}/likes")
    String likeQuestion(@PathVariable("id") Long id, @RequestParam("userEmail") String userEmail); 
    
    @PostMapping("/api/questions/{id}/approve")
    String approveQuestion(@PathVariable("id") Long id);
    
    @PostMapping("/api/questions/{id}/resolve")
    String resolveQuestion(@PathVariable("id") Long id);
    
    
}
