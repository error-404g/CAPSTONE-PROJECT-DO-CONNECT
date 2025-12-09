package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.beans.Contact;
import com.example.beans.User;
import com.example.repo.UserRepo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class UserService {
	
	
	@Autowired
	UserRepo repo;
	
//	@Autowired
//	RestTemplate restTemplate;
	

	@Autowired
	ContactFeignClient feignClient;
	
	// here it has retrieved the id from controller mapping which is 1 let's say
	@CircuitBreaker(name = "userService" , fallbackMethod="getUserByIdFallBack")
	public User getUserById(Integer userId)
	{
		
		User user = repo.findById(userId).get(); //  here it will return the user id , name and address 
		// So in user object we have to set the contact details object also
		List<Contact> contacts = feignClient.getSpecificContact(userId); // It will get the contact detail for that userId from feignClient (internally through feignClient(which we have created an interface there we have defined the microservice name and port and the getmapping url) it will connect to the contact-ms and produce the result)
		user.setContacts(contacts);
		return user;
	}
	
	public User getUserByIdFallBack(Integer uesrId , Throwable t)
	{
		
		System.out.println("Our services are currently unavailable please try later!");
		
		return new User();
	}
	

}

