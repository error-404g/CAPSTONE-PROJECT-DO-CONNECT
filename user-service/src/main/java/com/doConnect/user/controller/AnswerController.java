package com.doConnect.user.controller;

import com.doConnect.user.entity.Answer;
import com.doConnect.user.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class AnswerController {
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @PostMapping("/{questionId}/answers")
    public Answer addAnswer(@PathVariable Long questionId, @RequestBody Answer answer) {
        answer.setQuestionId(questionId);
        answer.setUserEmail("testuser@gmail.com");  // From session later
        return answerRepository.save(answer);
    }
    
    @GetMapping("/{questionId}/answers")
    public List<Answer> getAnswers(@PathVariable Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }
    
    @DeleteMapping("/answers/{id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable Long id, @RequestParam String userEmail) {
        Answer answer = answerRepository.findById(id).orElseThrow();
        if (!answer.getUserEmail().equals(userEmail)) {
            return ResponseEntity.status(403).body("Not authorized");
        }
        answerRepository.delete(answer);
        return ResponseEntity.ok("Answer deleted");
    }

}
