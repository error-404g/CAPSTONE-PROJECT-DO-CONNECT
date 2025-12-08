package com.doConnect.user.controller;

import com.doConnect.user.dto.UserRequest;
import com.doConnect.user.service.LikeService;
import com.doConnect.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.security.Principal; 

import com.doConnect.user.entity.Answer;
import com.doConnect.user.entity.Comment;
import com.doConnect.user.entity.Question;
import com.doConnect.user.entity.User;
import com.doConnect.user.repository.QuestionRepository;
import com.doConnect.user.repository.UserRepository;
import com.doConnect.user.repository.AnswerRepository;
import com.doConnect.user.repository.CommentRepository;

import org.springframework.web.servlet.mvc.support.RedirectAttributes; 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class WebController {

    @Autowired
    private UserService userService;
    @Autowired 
    private AnswerRepository answerRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String homePage(Model model, Authentication auth) {
    	 if (auth != null && auth.isAuthenticated()) {
             return "redirect:/dashboard";  // REDIRECT to UserController
         }
         return "home";  // or login
    }

    @GetMapping("/login")
    public String loginPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("error", model.containsAttribute("error"));
        model.addAttribute("logout", model.containsAttribute("logout"));
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal, 
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size) {
        
        System.out.println("🔥 DASHBOARD HIT - page=" + page + ", size=" + size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionsPage;
        
        if (principal != null) {
            //  USER'S OWN QUESTIONS (paginated)
            questionsPage = questionRepository.findByUserEmail(principal.getName(), pageable);
            
            // BELL: RECENTLY APPROVED questions (latest 5 approved)
            Pageable bellPageable = PageRequest.of(0, 5);  
            Page<Question> approvedQuestions = questionRepository
                .findByUserEmailAndApprovedTrueOrderByCreatedAtDesc(principal.getName(), bellPageable);
            model.addAttribute("questions", questionsPage.getContent());  // Main list
            model.addAttribute("bellQuestions", approvedQuestions.getContent());  // Bell dropdown
        } else {
            questionsPage = questionRepository.findByApprovedTrue(pageable);
            model.addAttribute("questions", questionsPage.getContent());
            model.addAttribute("bellQuestions", new ArrayList<>());  // Empty for guest
        }
        
        // Pagination data
        model.addAttribute("currentPage", questionsPage.getNumber());
        model.addAttribute("totalPages", questionsPage.getTotalPages());
        model.addAttribute("totalElements", questionsPage.getTotalElements());
        
        // User info
        String username = "guest";
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            username = user != null ? user.getUsername() : principal.getName();
        }
        model.addAttribute("userEmail", username);
        model.addAttribute("currentUserEmail", principal != null ? principal.getName() : "guest");
        
        return "dashboard";
    }

    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "register";
    }
  
    @GetMapping("/questions")  //  PUBLIC - ALL approved questions
    public String publicQuestions(Model model, 
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionsPage = questionRepository.findByApprovedTrue(pageable);  // ALL APPROVED!
        
        questionsPage.getContent().forEach(q -> q.setLikeCount(likeService.getQuestionLikeCount(q.getId())));
        
        model.addAttribute("questions", questionsPage.getContent());
        model.addAttribute("currentPage", questionsPage.getNumber());
        model.addAttribute("totalPages", questionsPage.getTotalPages());
        model.addAttribute("totalElements", questionsPage.getTotalElements());
        model.addAttribute("currentUserEmail", "guest");  // Public view
        
        return "questions";
    }

    
    @GetMapping("/user/questions")
    public String questions(Model model, Authentication authentication,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        // DECLARE TYPE Page<Question>
        Page<Question> questionsPage;  
        
        if (authentication != null && authentication.isAuthenticated()) {
            //  USER'S OWN questions (ALL - pending + approved)
            questionsPage = questionRepository.findByUserEmail(authentication.getName(), pageable);
        } else {
            //  GUEST/OTHERS: ONLY APPROVED
            questionsPage = questionRepository.findByApprovedTrue(pageable);
        }
        
        // Keep your like count logic
        questionsPage.getContent().forEach(q -> q.setLikeCount(likeService.getQuestionLikeCount(q.getId())));
        
        model.addAttribute("questions", questionsPage.getContent());
        model.addAttribute("currentPage", questionsPage.getNumber());
        model.addAttribute("totalPages", questionsPage.getTotalPages());
        model.addAttribute("totalElements", questionsPage.getTotalElements());
        model.addAttribute("currentUserEmail", authentication != null ? authentication.getName() : "guest");
        
        return "questions";
    }


   
    @GetMapping("/user/chat")
    public String showUserChat(Model model, Authentication authentication) {
        String email = authentication != null ? authentication.getName() : "guest@example.com";
        model.addAttribute("currentUserEmail", email);
        return "user-chat";
    }
    
    @GetMapping("/user/questions/search")
    public String searchQuestions(@RequestParam("query") String query, 
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,  // PAGING
                                 Model model, Authentication authentication) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionsPage = questionRepository
            .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query, pageable);  // PAGINATED SEARCH
        
        questionsPage.getContent().forEach(q -> q.setLikeCount(likeService.getQuestionLikeCount(q.getId())));  // KEEP LIKES
        
        model.addAttribute("questions", questionsPage.getContent());
        model.addAttribute("query", query);
        model.addAttribute("currentPage", questionsPage.getNumber());        //  ADD
        model.addAttribute("totalPages", questionsPage.getTotalPages());     //  ADD
        model.addAttribute("totalElements", questionsPage.getTotalElements()); //  ADD
        model.addAttribute("currentUserEmail", authentication != null ? authentication.getName() : "guest");
        
        return "questions";
    }



    @GetMapping("/user/ask-question")
    public String askQuestion() {
        return "ask-question";
    }

    @GetMapping("/user/questions/{id}")
    public String questionDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Question question = questionRepository.findById(id).orElse(null);
        List<Answer> answers = answerRepository.findByQuestionId(id);
        List<Comment> comments = commentRepository.findByQuestionIdOrderByCreatedAtDesc(id);  
        
        model.addAttribute("question", question);
        model.addAttribute("answers", answers);
        model.addAttribute("comments", comments);  // ADD THIS
        model.addAttribute("currentUserEmail", authentication != null ? authentication.getName() : "guest"); 
        return "question-details";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Principal principal) {
        //  PERFECT - Already dynamic for ANY admin!
        String adminEmail = principal != null ? principal.getName() : "admin@doconnect.com";
        model.addAttribute("currentUserEmail", adminEmail);
        
        model.addAttribute("users", userService.getAllUsers());
        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        
        //  OPTIMIZE - Avoid double findAll()
        long pendingCount = questions.stream()
            .filter(q -> !q.isApproved())
            .count();
        model.addAttribute("pendingCount", (int) pendingCount);
        
        return "admin-dashboard";
    }
    
    @GetMapping("/admin/questions")
    public String adminQuestions(Model model, Principal principal,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) Boolean approved) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionsPage;
        
        if (approved != null) {
            // Filter by approval status
            if (approved) {
                questionsPage = questionRepository.findByApprovedTrue(pageable);
            } else {
                questionsPage = questionRepository.findByApprovedFalse(pageable);
            }
        } else {
            questionsPage = questionRepository.findAll(pageable);
        }
        
        model.addAttribute("questions", questionsPage.getContent());
        model.addAttribute("currentPage", questionsPage.getNumber());
        model.addAttribute("totalPages", questionsPage.getTotalPages());
        model.addAttribute("totalElements", questionsPage.getTotalElements());
        model.addAttribute("approvedFilter", approved);
        model.addAttribute("currentUserEmail", principal != null ? principal.getName() : "admin");
        
        return "admin-questions";
    }


    @GetMapping("/admin/users")
    public String adminUsers(Model model, 
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String search,
                            Principal principal) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage;
        
        if (search != null && !search.trim().isEmpty()) {
            usersPage = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }
        
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalElements", usersPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("currentUserEmail", principal != null ? principal.getName() : "admin");
        
        return "admin-users";
    }


    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRequest") UserRequest userRequest,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.register(userRequest);
            return "redirect:/login?registerSuccess";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    
    @PostMapping("/user/ask-question")
    public String createQuestionPost(@RequestParam String title,
                                   @RequestParam String content,
                                   Principal principal) {  // principal may be null!
        
        // NULL-SAFE: Check principal FIRST!
        String userEmail = (principal != null && principal.getName() != null) 
                          ? principal.getName() 
                          : "anonymous";
        
        Question question = new Question();
        question.setTitle(title);
        question.setContent(content);
        question.setUserEmail(userEmail);
        question.setApproved(false); 
        question.setResolved(false);  
        questionRepository.save(question);
        

     // TRY-CATCH EMAIL - DON'T BREAK REDIRECT!
        try {
            Map<String, Object> emailRequest = Map.of(
                "toEmail", "giridharank2610@gmail.com",
                "subject", "🆕 New Question Posted - DoConnect",
                "message", "Title: " + title + " by " + principal.getName(),
                "type", "NEW_QUESTION"
            );
            restTemplate.postForEntity("http://email-notification-service/api/notifications/email", 
                emailRequest, String.class);
            System.out.println("✅ Email sent!");
        } catch (Exception e) {
            System.out.println("⚠️ Email failed (question saved): " + e.getMessage());
            // DON'T THROW - LET REDIRECT HAPPEN!
        }
        return "redirect:/user/questions";
    }

    @PostMapping("/user/questions/{id}/answer")  
    public String postAnswer(@PathVariable Long id,
                            @RequestParam String content,
                            Principal principal) {
    	System.out.println("POST ANSWER HIT! id=" + id + " content=" + content);  
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setQuestionId(id);
        answer.setUserEmail(principal.getName());
        answerRepository.save(answer);
        
        try {
            Map<String, Object> emailRequest = Map.of(
                "toEmail", "giridharank2610@gmail.com",
                "subject", "💬 New Answer Posted - DoConnect",
                "message", "Answer on Q:" + id + " by " + principal.getName(),
                "type", "NEW_ANSWER"
            );
            restTemplate.postForEntity("http://email-notification-service/api/notifications/email", 
                emailRequest, String.class);
        } catch (Exception e) {
            System.out.println("⚠️ Email failed (answer saved): " + e.getMessage());
        }
        
        return "redirect:/user/questions/" + id;
    }
    
 // ADD NEW METHOD for comments
    @PostMapping("/user/questions/{id}/comment")
    public String postComment(@PathVariable Long id,
                              @RequestParam String content,
                              Principal principal) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setQuestionId(id);
        comment.setUserEmail(principal.getName());
        commentRepository.save(comment);

        try {
            System.out.println("💭 SENDING COMMENT EMAIL for qId=" + id);

            Map<String, Object> emailRequest = Map.of(
                "toEmail", "giridharank2610@gmail.com",
                "subject", "💭 New Comment Posted - DoConnect",
                "message", "Comment on Q:" + id + " by " + principal.getName(),
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

        return "redirect:/user/questions/" + id;
    }


    
    @PostMapping("/api/questions/{id}")
    public String deleteQuestion(@PathVariable Long id, 
                                @RequestParam String userEmail,
                                RedirectAttributes redirectAttributes) {
        Question question = questionRepository.findById(id).orElse(null);
        
        if (question != null && question.getUserEmail().equals(userEmail)) {
            questionRepository.delete(question);
            redirectAttributes.addFlashAttribute("success", "Question deleted!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cannot delete this question");
        }
        
        return "redirect:/user/questions";
    }
    
    @PostMapping("/api/comments/{id}")
    public String deleteComment(@PathVariable Long id, 
                                @RequestParam String userEmail,
                                RedirectAttributes redirectAttributes) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null && comment.getUserEmail().equals(userEmail)) {
            commentRepository.delete(comment);
            redirectAttributes.addFlashAttribute("success", "Comment deleted!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cannot delete this comment");
        }
        return "redirect:/user/questions/" + (comment != null ? comment.getQuestionId() : "");
    }

    @PostMapping("/api/answers/{id}")
    public String deleteAnswer(@PathVariable Long id, 
                               @RequestParam String userEmail,
                               RedirectAttributes redirectAttributes) {
        Answer answer = answerRepository.findById(id).orElse(null);
        if (answer != null && answer.getUserEmail().equals(userEmail)) {
            answerRepository.delete(answer);
            redirectAttributes.addFlashAttribute("success", "Answer deleted!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cannot delete this answer");
        }
        return "redirect:/user/questions/" + (answer != null ? answer.getQuestionId() : "");
    }

}
