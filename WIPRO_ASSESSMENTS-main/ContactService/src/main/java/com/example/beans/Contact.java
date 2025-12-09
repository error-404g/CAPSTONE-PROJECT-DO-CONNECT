package com.example.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
	
	
	@Id
	Integer conId;
	String email;
	String phone;
	Integer userId;

}
