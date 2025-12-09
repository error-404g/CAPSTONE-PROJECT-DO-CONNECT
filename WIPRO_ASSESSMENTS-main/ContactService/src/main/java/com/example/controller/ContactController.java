package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.beans.Contact;
import com.example.repo.ContactRepo;
import com.example.service.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {

	@Autowired
	ContactService service;
	
	@Autowired
	ContactRepo repo;
	
	@GetMapping("{userId}")
	public List<Contact> getUserById(@PathVariable("userId") Integer userId)
	{
		//
		return service.getContactByUserById(userId);
		
		
	}
	
	@GetMapping("/all")
	public List<Contact> getAllUsers()
	{
		return (List<Contact>) repo.findAll();
	}
	
}
