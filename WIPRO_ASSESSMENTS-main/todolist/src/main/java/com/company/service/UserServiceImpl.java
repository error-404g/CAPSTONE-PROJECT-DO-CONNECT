package com.company.service;

import com.company.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.repository.UserRepository;

@Service
public class UserServiceImpl {
	
	@Autowired 
	UserRepository usersRepository;
	
	

	public boolean findUser(com.company.model.User u) {
		// TODO Auto-generated method stub
		return usersRepository.findUser(u.getUsername(),u.getPassword());
	}
	
	
	
	
	
	
	 public String registerUser(User user) {
	        if (usersRepository.userExists(user.getUsername())) {
	            return "Username already exists!";
	        }
	        usersRepository.saveUser(user);
	        return "User registered successfully!";
	    }

}
