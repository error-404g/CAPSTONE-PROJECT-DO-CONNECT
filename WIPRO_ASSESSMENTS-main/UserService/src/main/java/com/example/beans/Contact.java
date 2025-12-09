package com.example.beans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//ContactDTO
// Remove the JPA annotations because we don't want to save in the database through repository because it's like a DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
	
	// Here we are using only for getter and setter methods 

	Integer conId;
	String email;
	String phone;
	Integer userId;

}
