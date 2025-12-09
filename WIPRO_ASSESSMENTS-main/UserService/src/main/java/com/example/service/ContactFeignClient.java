package com.example.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.beans.Contact;

@FeignClient(name ="contact-ms" , url="http://localhost:8082")
public interface ContactFeignClient {
	
	
	//http://localhost:8082/contacts/1
	
	@GetMapping("/contacts/{userId}")
	List<Contact> getSpecificContact(@PathVariable("userId") int userId);

}
