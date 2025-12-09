package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.beans.User;
import com.example.repo.UserRepo;

import jakarta.annotation.PostConstruct;

// to load dummy data when you run or execute this application
@Component
public class UserDataLoader {
	
	@Autowired
	UserRepo repo;
	
	@Autowired
	public UserDataLoader(UserRepo repo) {
		super();
		this.repo = repo;
	}

	
   @PostConstruct
	public void loadDummyData()
	{
		
		
		User user1 = new User(1,"User1","Address1");
		User user2 = new User(2,"User2","Address2");
		User user3 = new User(3,"User3","Address3");
		User user4 = new User(4,"User4","Address4");
		
		
		repo.save(user1);
		repo.save(user2);
		repo.save(user3);
		repo.save(user4);
		
		System.out.println("Data loaded safely");
	}

}
