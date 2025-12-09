package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.beans.Contact;
import com.example.repo.ContactRepo;

@Service
public class ContactService {
	
	
	@Autowired
	ContactRepo repo;
	
//	@Autowired
//	RestTemplate restTemplate;
	

	
	
	public List<Contact> getContactByUserById(Integer userId)
	{
		
		return repo.findByUserId(userId);
	}
	

}
