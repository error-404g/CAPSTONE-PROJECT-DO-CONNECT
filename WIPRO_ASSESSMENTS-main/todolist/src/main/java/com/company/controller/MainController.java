package com.company.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.model.Task;
import com.company.model.User;
import com.company.service.TaskService;
import com.company.service.UserServiceImpl;



@Controller
public class MainController {

	@Autowired
	private TaskService taskService;
	
	@Autowired 
	private UserServiceImpl userService;
	
	 @GetMapping("/") // Handle requests to the home page
	    public String showHomePage() {
	        return "home";  // This will load home.html from templates folder
	    }
	 
	 

	 @GetMapping("/register")
	    public String showRegisterPage() {
	        return "register";  // Load register.html
	    }

	 
	 
	 //When you will pass the values from register.html it will take the parameters username and password ,
	 //and bind this with the variables as username and password which is of String type
	    @PostMapping("/register")
	    public String registerUser(@RequestParam("username") String username,
	                               @RequestParam("password") String password,
	                               Model model) {
	        
	        User user = new User(username, password);
	        String result = userService.registerUser(user);

	        if (result.equals("User registered successfully!")) {
	            return "redirect:/log";  // Redirect to login page
	        } else {
	            model.addAttribute("message", result);
	            return "register";  // Stay on register page if username exists
	        }
	    }
	 
	 @GetMapping("/log")
	 public String showLoginPage() {
	     return "login";  // Loads login.html
	 }
	
	 @PostMapping("/authenticate")
	 public String AuthenticateUser(@RequestParam("username") String username,
	                                @RequestParam("pword") String password,
	                                Model model, HttpServletRequest req) {
	     
	     User u = new User(username, password);

	     if (userService.findUser(u)) {
	         HttpSession session = req.getSession();
	         session.setAttribute("username", username);
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
	

	    
	@GetMapping("/addtask")
	public String index(Model model)
	{
		model.addAttribute("tasks",taskService.getAllTasks());
		return "displayalltask"; 
	}
		
	@GetMapping("/add")
	public String addTaskForm(Model model)
	{
		model.addAttribute("task",new Task());
		System.out.println("Add Task");
		return "add"; 
	}
	
	@PostMapping("/save")
	public String saveTask(@ModelAttribute Task task)
	{
		
		taskService.saveTask(task);
		return "redirect:/addtask";
	}
	
	
	
	//difference between Model , @ModelAndView , @ModelAttribute
	// difference between @ResponseBody , @PathVariable , @RequestParam
	//difference between return "redirect:/service"  and return "service"
	// difference between JdbcTemplate and JpaRepository
	/*several annotation we have used as  @Entity, @Component ,@ComponentScan, @Service , @Id , @GeneratedValue ,
	 * @Autowired , @GetMapping ( similarly put, post ,delete ,patch) , @Repository ,@Bean , @SpringBootApplication
	 * @EnableJAutoProxy , @Advice , @Before ,@After , @AroundAdvice  
	 * Some lombok annotations :  @Data , @AllArgsConstructor , @NoArgsConstructor 
	*/
	
}
