package com.example.beans;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
	
	@Id
	Integer userId;
	String userName;
	String userAddr;
	
	@Transient    // which will not be persistent // how this user service will communicate with contact service ---- through Feign Client or RestTemplate
	List<Contact> contacts;
	
	
	public User(Integer userId, String userName, String userAddr) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userAddr = userAddr;
	}

	
	// so in this constructor we are setting the list of contacts using feignclient as  below which we have defined in user service class:
	//List<Contact> contacts = feignClient.getSpecificContact(userId); // It will get the contact detail for that userId from feignClient (internally through feignClient(which we have created an interface there we have defined the microservice name and port and the getmapping url) it will connect to the contact-ms and produce the result)
	//user.setContacts(contacts);

	public User(Integer userId, String userName, String userAddr, List<Contact> contacts) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userAddr = userAddr;
		this.contacts = contacts;
	}
	
	
	

}
