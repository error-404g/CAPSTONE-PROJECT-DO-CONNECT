package com.doConnect.user.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.doConnect.user.dto.QuestionRequest;
import com.doConnect.user.dto.QuestionResponse;
import com.doConnect.user.entity.Comment;
import com.doConnect.user.entity.Question;
import com.doConnect.user.repository.AnswerRepository;
import com.doConnect.user.repository.CommentRepository;
import com.doConnect.user.repository.QuestionRepository;
import com.doConnect.user.service.CommentService;
import com.doConnect.user.service.LikeService;
import com.doConnect.user.service.QuestionService;

import jakarta.servlet.http.HttpServletRequest;

@Controller  // ✅ CHANGED: Controller (NOT RestController) for Thymeleaf!
@RequestMapping("/api/questions")
public class QuestionController {
    
    @Autowired private QuestionService questionService;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private CommentService commentService;
    @Autowired private CommentRepository commentRepository;
    @Autowired private LikeService likeService;
    @Autowired private RestTemplate restTemplate;
    @Autowired private AnswerRepository answerRepository;
    
    // ✅ HELPER: Get current user email
    private String getCurrentUserEmail(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "guest@example.com";
        }
        return auth.getName();
    }
    
    @PostMapping
    @ResponseBody  // ✅ REST API
    public QuestionResponse createQuestion(@RequestBody QuestionRequest request) {
        return questionService.createQuestion(request);
    }
    
    @GetMapping 
    @ResponseBody// /api/questions - PUBLIC (users see this)
    public List<QuestionResponse> getAllQuestions() {
        return questionService.getApprovedQuestions();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<Question> findById(@PathVariable Long id) {
        return questionService.findById(id);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public Question updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        question.setId(id);
        return questionService.save(question);
    }
    
    @GetMapping("/search")
    @ResponseBody
    public List<QuestionResponse> searchQuestions(@RequestParam String query) {
        return questionService.searchQuestions(query);
    }

    @GetMapping("/approved")
    @ResponseBody
    public List<QuestionResponse> getApprovedQuestions() {
        return questionService.getApprovedQuestions();
    }

    @GetMapping("/resolved")
    @ResponseBody
    public List<QuestionResponse> getResolvedQuestions() {
        return questionService.getResolvedQuestions();
    }
    
 // user-service QuestionController - PUBLIC for admin!
    @GetMapping("/admin/all")
    @ResponseBody  // ✅ CRITICAL! Force JSON, NO template!
    public List<QuestionResponse> getAllQuestionsForAdmin(HttpServletRequest request) {
        System.out.println("🔥 🔥 🔥 ADMIN ALL QUESTIONS!");
        List<QuestionResponse> all = questionService.getAllQuestions();
        System.out.println("Returning " + all.size() + " questions");
        return all;
    }



    // FIXED: Thymeleaf template endpoint (DIFFERENT path!)
    @GetMapping("/view/{id}")  // /api/questions/view/42
    public String questionDetails(@PathVariable Long id, Model model, Authentication auth) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return "redirect:/user/questions";
        }
        
        model.addAttribute("question", question);
        // SAFE: Empty lists if repositories don't have custom methods
        model.addAttribute("answers", answerRepository.findAll());
        model.addAttribute("comments", commentRepository.findAll());
        model.addAttribute("currentUserEmail", getCurrentUserEmail(auth));
        
        // NULL SAFE ADMIN CHECK
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("showAdminActions", isAdmin);
        
        return "question-details";  // ✅ Thymeleaf template!
    }

    // NEW: Approve question
    @PostMapping("/{id}/approve")
    @ResponseBody
    public ResponseEntity<String> approveQuestion(@PathVariable Long id) {
        System.out.println("🔥 USER-SERVICE APPROVE: " + id);
        questionService.approveQuestion(id);
        return ResponseEntity.ok("✅ Question approved: " + id);
    }

    // NEW: Resolve question
    @PostMapping("/{id}/resolve")
    @ResponseBody
    public ResponseEntity<String> resolveQuestion(@PathVariable Long id) {
        questionService.resolveQuestion(id);
        return ResponseEntity.ok("🔒 Question resolved: " + id);
    }
    
    @PostMapping("/{id}/likes")
    @ResponseBody
    public ResponseEntity<String> likeQuestion(@PathVariable Long id, @RequestParam String userEmail) {
        likeService.likeQuestion(id, userEmail);
        return ResponseEntity.ok("Liked question " + id);
    }

    @GetMapping("/{id}/likes/count")
    @ResponseBody
    public ResponseEntity<Long> getLikeCount(@PathVariable Long id) {
        return ResponseEntity.ok(likeService.getQuestionLikeCount(id));
    }
    
    @PostMapping("/{id}/comments")
    @ResponseBody
    public Comment addComment(@PathVariable Long id,
                              @RequestParam String userEmail,
                              @RequestParam String text) {
        Comment saved = commentService.addCommentToQuestion(id, userEmail, text);
        System.out.println("✅ NEW COMMENT SAVED: ID=" + saved.getId());

        try {
            Map<String, Object> emailRequest = Map.of(
                "toEmail", "giridharank2610@gmail.com",
                "subject", "💭 New Comment Posted - DoConnect",
                "message", "Comment on Q:" + id + " by " + userEmail,
                "type", "NEW_COMMENT"
            );
            restTemplate.postForEntity(
                "http://email-notification-service/api/notifications/email",
                emailRequest,
                String.class
            );
            System.out.println("✅ COMMENT EMAIL REQUEST SENT");
        } catch (Exception e) {
            System.out.println("⚠️ Email failed (comment saved): " + e.getMessage());
        }
        return saved;
    }

    @GetMapping("/{id}/comments")
    @ResponseBody
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsForQuestion(id));
    }
    
    @DeleteMapping("/comments/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestParam String userEmail) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        if (!comment.getUserEmail().equals(userEmail)) {
            return ResponseEntity.status(403).body("Not authorized");
        }
        commentRepository.delete(comment);
        return ResponseEntity.ok("Comment deleted");
    }
    
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        System.out.println("🔥 USER-SERVICE DELETE: " + id);
        questionRepository.deleteById(id);
        System.out.println("✅ DELETED ID: " + id);
        return ResponseEntity.ok("Deleted");
    }
}
