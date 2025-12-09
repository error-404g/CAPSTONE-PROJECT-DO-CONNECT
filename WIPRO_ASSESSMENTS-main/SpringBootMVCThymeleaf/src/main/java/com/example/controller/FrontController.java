package com.example.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.SpringBootMvcThymeleafApplication;
import com.example.model.User;
import com.example.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class FrontController {

    private final SpringBootMvcThymeleafApplication springBootMvcThymeleafApplication;
	
	@Autowired
	public UserService userService;

    FrontController(SpringBootMvcThymeleafApplication springBootMvcThymeleafApplication) {
        this.springBootMvcThymeleafApplication = springBootMvcThymeleafApplication;
    }
	
	// 1 . http://localhost:8080/
	 @GetMapping("/") // Handle requests to the home page
	    public String showHomePage() {
	        return "home";  // This will load home.html from templates folder
	    }
	 

	 
	  // 2. To display login page
	 @GetMapping("/log")
	 public String showLoginPage() {
	     return "login";  // Loads login.html
	 }
	 
	 // 3. To display register page
	 @GetMapping("/register")
	    public String showRegisterPage() {
	        return "register";  // Load register.html
	    }
	 
	 //4. First you have to register which is post mapping
	 //When you will pass the values from register.html it will take the parameters username and password ,
	 //and bind this with the variables as username and password which is of String type
	    @PostMapping("/register")
	    public String registerUser(@RequestParam("username") String username,
	                               @RequestParam("password") String password,
	                               Model model) {
	        
	    	// Model/entity class object and in constructor we have passed the values 
	        User user = new User(username, password);
	        String result = userService.registerUser(user);

	        if (result.equals("User registered successfully!")) {
	            return "redirect:/log";  // Redirect to login page
	        } else {
	            model.addAttribute("message", result);
	            return "register";  // Stay on register page if username exists
	        }
	    }
	    
	    
	    //7. After registering and updating into the database via repository now we will do the login process 
	    //From login.html it will post and url handler is /authenticate --  it will fetch the values via @RequestParam and bind with the variables 
	    @PostMapping("/authenticate")
		 public String AuthenticateUser(@RequestParam("username") String username,
		                                @RequestParam("pword") String password,
		                                Model model, HttpServletRequest req) {
		     
		     User u = new User(username, password);

		     if (userService.findUser(u)) 
		     
		     {
		         HttpSession session = req.getSession();
		         session.setAttribute("username", username);
		            System.out.println(session.getCreationTime());
		            // If you are retrieving the value from session using getAttribute they hereitself 
		            // you can set in a model object with addAttribute and directly return to service.html inplace of redirect:/service which means
		            // it will redirect to the same controller to search for service url handler mapping 
		          //  model.addAttribute("username", session.getAttribute("username"));
		         return "redirect:/service";  // Redirect to success page
		     }

		     model.addAttribute("message", "Invalid Username/Password");
		     return "display";  // Show error page if login fails
		 }
	    
	    @GetMapping("/service")
	    public String showServicePage(HttpServletRequest req, Model model) {
	        HttpSession session = req.getSession(false);
	        if (session != null && session.getAttribute("username") != null) {
	            model.addAttribute("username", session.getAttribute("username"));
	            return "service";  // Show service.html
	        }
	        return "redirect:/log";  // Redirect to login if session doesn't exist
	    }
	
	 
	 
}
