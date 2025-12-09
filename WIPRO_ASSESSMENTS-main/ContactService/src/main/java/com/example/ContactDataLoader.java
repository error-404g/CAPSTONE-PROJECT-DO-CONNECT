package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.beans.Contact;
import com.example.repo.ContactRepo;

import jakarta.annotation.PostConstruct;

@Component
public class ContactDataLoader {
	
	@Autowired
	ContactRepo repo;
	
	@Autowired
	public ContactDataLoader(ContactRepo repo) {
		super();
		this.repo = repo;
	}

	
   @PostConstruct
	public void loadDummyData()
	{
		
		Contact contact1 = new Contact(1,"email1@example.com" , "343434" , 1);
	   
		Contact contact2 = new Contact(2,"email2@example.com" , "56565" , 1);
		
		Contact contact3 = new Contact(3,"email3@example.com" , "653434" , 1);
		
		Contact contact4 = new Contact(4,"email4@example.com" , "343434" , 2);
		   
		Contact contact5 = new Contact(5,"email5@example.com" , "56567665" , 2);
		
		Contact contact6 = new Contact(6,"email6@example.com" , "653435434" , 3);
		
		Contact contact7 = new Contact(7,"email7@example.com" , "65533434" , 3);
	
		Contact contact8 = new Contact(8,"email8@example.com" , "2353434" , 3);
		
		Contact contact9 = new Contact(9,"email9@example.com" , "653434" , 4);
		
		repo.save(contact1);
		repo.save(contact2);
		repo.save(contact3);
		repo.save(contact4);
		repo.save(contact5);
		repo.save(contact6);
		repo.save(contact7);
		repo.save(contact8);
		repo.save(contact9);
		
		
		System.out.println("Data loaded safely");
	}

}
