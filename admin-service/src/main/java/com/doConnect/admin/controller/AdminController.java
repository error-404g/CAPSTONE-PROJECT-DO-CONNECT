package com.doConnect.admin.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.doConnect.admin.client.AdminUserClient;
import com.doConnect.admin.client.AnswerClient;
import com.doConnect.admin.client.CommentClient;
import com.doConnect.admin.client.QuestionClient;
import com.doConnect.admin.client.UserClient;
import com.doConnect.admin.dto.AdminRequest;
import com.doConnect.admin.entity.Admin;
import com.doConnect.admin.repository.AdminRepository;
import com.doConnect.admin.service.AdminService;
import com.doConnect.admin.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import com.doConnect.admin.dto.QuestionResponse;
import com.doConnect.admin.dto.Answer;
import com.doConnect.admin.dto.Question;
import com.doConnect.admin.dto.User;

@Controller
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:8081")  // user-service
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private QuestionClient questionClient;
    @Autowired 
    private CommentClient commentClient; 
    @Autowired
    private AnswerClient answerClient;
    @Autowired
    private AdminUserClient adminUserClient;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired 
    private RestTemplate restTemplate; 
    @Autowired
    private JwtUtil jwtUtil; 
    
    
    // HTML PAGES
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // login.html 
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("currentUserEmail", authentication.getName());
        
        //  DON'T BLOCK DASHBOARD!
        try {
            List<User> users = userClient.getAllUsers();
            model.addAttribute("users", users);
        } catch (Exception e) {
            model.addAttribute("users", new ArrayList<>());
            System.out.println("⚠️ User service unavailable: " + e.getMessage());
        }
        
        try {
            List<QuestionResponse> questions = questionClient.getAllQuestions();
            long pendingCount = questions.stream()
                .filter(q -> q != null && !Boolean.TRUE.equals(q.isApproved()))
                .count();
            model.addAttribute("questions", questions);
            model.addAttribute("pendingCount", pendingCount);
        } catch (Exception e) {
            model.addAttribute("questions", new ArrayList<>());
            model.addAttribute("pendingCount", 0);
            System.out.println("⚠️ Question service unavailable: " + e.getMessage());
        }
        
        return "admin-dashboard";
    }


    @GetMapping("/questions")
    public String questions(Model model) {
        System.out.println("🔥 ADMIN QUESTIONS DASHBOARD!");
        try {
            List rawQuestions = restTemplate.getForObject(
                "http://localhost:8081/api/questions/admin/all", 
                List.class
            );
            System.out.println("✅ RAW: " + (rawQuestions != null ? rawQuestions.size() : 0));
            model.addAttribute("questions", rawQuestions);  // Raw Maps OK!
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
            model.addAttribute("questions", List.of());
        }
        return "questions";
    }


    // HELPER METHOD
    private QuestionResponse mapToQuestionResponse(Map<String, Object> map) {
        QuestionResponse q = new QuestionResponse();
        q.setId(((Number) map.get("id")).longValue());
        q.setTitle((String) map.get("title"));
        q.setContent((String) map.get("content"));
        q.setUserEmail((String) map.get("userEmail"));
        q.setApproved(map.get("approved") != null ? 
            Boolean.valueOf(map.get("approved").toString()) : false);
        q.setResolved(map.get("resolved") != null ? 
            Boolean.valueOf(map.get("resolved").toString()) : false);
        
        //   Handle null or string date
        Object createdAt = map.get("createdAt");
        if (createdAt instanceof String) {
            try {
                // Parse if string, set dummy if fails
                q.setCreatedAt(LocalDateTime.parse((String) createdAt));
            } catch (Exception e) {
                q.setCreatedAt(LocalDateTime.now());
            }
        } else {
            q.setCreatedAt(LocalDateTime.now());
        }
        
        return q;
    }

    @GetMapping("/questions/{id}")
    public String adminQuestionDetail(@PathVariable Long id, Model model, Authentication authentication) {
        QuestionResponse question = questionClient.getQuestion(id);
        List<Answer> answers = answerClient.getAnswers(id);   // AnswerClient

        model.addAttribute("question", question);
        model.addAttribute("answers", answers);               // pass as "answers"
        model.addAttribute("showAdminActions", true);
        model.addAttribute("currentUserEmail", authentication.getName());
        return "question-detail";
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("adminRequest", new AdminRequest()); // For form binding
        return "register";  // register.html 
    }
    
    @GetMapping("/admins")
    public String manageAdmins(Model model) {
        List<User> admins = adminUserClient.getAdmins();
        model.addAttribute("admins", admins);
        return "admin-manage-admins";
    }
    
   
    @GetMapping("/users")
    public String adminUsers(Model model, Authentication authentication,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String search) {
        
        // AUTH CHECK FIRST - BEFORE ANY LOGIC!
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/admin/login";
        }
        
        // SIMPLE JSON → Map parsing
        List<Map<String, Object>> allUsers = new ArrayList<>();
        
        try {
            // USE AUTOWIRED RestTemplate
            List<Map<String, Object>> rawUsers = restTemplate.getForObject(
                "http://localhost:8081/api/users", 
                List.class
            );
            
            if (rawUsers != null) {
                allUsers = rawUsers;
                System.out.println("✅ AdminController: Loaded " + allUsers.size() + " users");
            }
        } catch (Exception e) {
            System.out.println("❌ AdminController error: " + e.getMessage());
        }
        
        // FILTER + PAGINATION (Map data)
        if (search != null && !search.trim().isEmpty()) {
            allUsers = allUsers.stream()
                .filter(u -> {
                    String username = String.valueOf(u.get("username"));
                    String email = String.valueOf(u.get("email"));
                    return username.toLowerCase().contains(search.toLowerCase()) ||
                           email.toLowerCase().contains(search.toLowerCase());
                })
                .collect(Collectors.toList());
        }
        
        int totalElements = allUsers.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = Math.min(page * size, allUsers.size());
        int end = Math.min(start + size, allUsers.size());
        List<Map<String, Object>> usersPage = allUsers.subList(start, end);
        
        model.addAttribute("users", usersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("search", search);
        model.addAttribute("currentUserEmail", authentication.getName());  // CORRECT!
        
        return "admin-users";
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        System.out.println("🔥 Admin login attempt: " + email);

        try {
            Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + email));

            System.out.println("🔥 Admin found in DB, encoded password = " + admin.getPassword());
            System.out.println("🔥 Raw password from request = " + password);

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                System.out.println("❌ Password mismatch");
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }

            String jwt = jwtUtil.generateToken(email, "ROLE_ADMIN");

            Map<String, Object> response = Map.of(
                "success", true,
                "token", jwt,
                "role", "ADMIN",
                "email", email
            );

            System.out.println("✅ Login success, JWT = " + jwt);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }




    @PostMapping("/admins")
    public String createAdminFromForm(@ModelAttribute("adminForm") User adminForm) {
        adminUserClient.createAdmin(adminForm);
        return "redirect:/admin/admins";
    }
    
    @GetMapping("/chat")
    public String showChatPage(Model model, Authentication authentication) {
        model.addAttribute("currentUserEmail",
                authentication != null ? authentication.getName() : "admin@doconnect.com");
        return "admin-chat"; // must match template name
    }

    @PostMapping("/admins/{id}/delete")
    public String deleteAdminFromForm(@PathVariable Long id) {
        adminUserClient.deleteAdmin(id);
        return "redirect:/admin/admins";
    }


    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute AdminRequest adminRequest, Model model) {  // ← RIGHT
        try {
            Admin admin = new Admin();
            admin.setUsername(adminRequest.getUsername());  // ←Change from adminForm
            admin.setEmail(adminRequest.getEmail());
            admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));  // ← Change
            adminRepository.save(admin);
            
            // Update user-service call too
            User userForTable = new User();
            userForTable.setUsername(adminRequest.getUsername());
            userForTable.setEmail(adminRequest.getEmail());
            userForTable.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
            userForTable.setRole("ROLE_ADMIN");
            userForTable.setIsActive(true);
            adminUserClient.createAdmin(userForTable);
            
            return "redirect:/admin/login?registered=true";  // Go back to login first

        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("adminRequest", adminRequest);  //  ADD THIS
            return "register";  //  Changed from "admin-register"
        }
    }


    
    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<?> apiRegisterAdmin(@RequestBody AdminRequest adminRequest) {
        try {
            adminService.register(adminRequest);
            return ResponseEntity.ok(Map.of(
                "message", "Admin registered successfully",
                "email", adminRequest.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "error", e.getMessage()
            ));
        }
    }

    // === APPROVE/RESOLVE APIs (JSON) ===
    @PostMapping("/questions/{id}/approve")
    @ResponseBody  // ✅ CRITICAL!
    public ResponseEntity<String> approveQuestion(@PathVariable Long id) {
        System.out.println("🔥 🔥 ADMIN APPROVE: " + id);
        
        try {
            //  DIRECT RestTemplate call to user-service
            String result = restTemplate.postForObject(
                "http://localhost:8081/api/questions/" + id + "/approve",
                null,
                String.class
            );
            
            System.out.println("✅ APPROVE SUCCESS: " + result);
            return ResponseEntity.ok("✅ Question approved: " + id + " | " + result);
            
        } catch (Exception e) {
            System.out.println("❌ APPROVE FAILED: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Approve failed: " + e.getMessage());
        }
    }



    @PostMapping("/questions/{id}/resolve")
    @ResponseBody  // Forces JSON response
    public ResponseEntity<String> resolveQuestion(@PathVariable Long id) {
        adminService.resolveQuestion(id);
        return ResponseEntity.ok("🔒 Question resolved: " + id);
    }

    @PostMapping("/users/block/{id}")
    public String blockUserForm(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,      //  Remove HttpServletRequest
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String search) {
        
        System.out.println("🔥 BLOCK ID=" + id);
        try {
            restTemplate.postForObject("http://localhost:8081/api/users/admin/users/" + id + "/block", null, String.class);
        } catch (Exception e) {
            System.out.println("❌ BLOCK ERROR: " + e.getMessage());
        }
        
        // SIMPLE REDIRECT - Filter handles JWT!
        return "redirect:/admin/users?page=" + page + "&size=" + size + 
               (search != null ? "&search=" + search : "");
    }


    @PostMapping("/users/unblock/{id}")
    public String unblockUserForm(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String search) {
        
        System.out.println("🔥 UNBLOCK ID=" + id);
        try {
            restTemplate.postForObject("http://localhost:8081/api/users/admin/users/" + id + "/unblock", null, String.class);
        } catch (Exception e) {
            System.out.println("❌ UNBLOCK ERROR: " + e.getMessage());
        }
        
        // SIMPLE REDIRECT - Same as block
        return "redirect:/admin/users?page=" + page + "&size=" + size + 
               (search != null ? "&search=" + search : "");
    }


    @PostMapping("/api/questions/{id}/likes")
    @ResponseBody
    public ResponseEntity<String> proxyLikeQuestion(@PathVariable Long id, @RequestParam String userEmail) {
        String result = questionClient.likeQuestion(id, userEmail);
        return ResponseEntity.ok(result);  // Wrap String in ResponseEntity
    }
    
    @PostMapping("/questions/{id}/delete")
    @ResponseBody  
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        System.out.println("🔥 🔥 ADMIN DELETE: " + id);
        
        try {
            String result = restTemplate.postForObject(
                "http://localhost:8081/api/questions/delete/" + id,
                null,
                String.class
            );
            
            System.out.println("✅ DELETE SUCCESS: " + result);
            return ResponseEntity.ok("✅ Question deleted: " + id);
            
        } catch (Exception e) {
            System.out.println("❌ DELETE FAILED: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Delete failed: " + e.getMessage());
        }
    }

    
    @GetMapping("/api/questions/{id}/likes/count")
    @ResponseBody
    public ResponseEntity<Long> proxyLikeCount(@PathVariable Long id) {
        Long count = questionClient.getLikeCount(id);
        return ResponseEntity.ok(count);  // Wrap Long in ResponseEntity
    }


}
