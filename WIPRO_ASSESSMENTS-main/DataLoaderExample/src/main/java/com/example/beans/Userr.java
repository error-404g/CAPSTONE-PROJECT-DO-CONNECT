package com.example.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Userr {
	
	@Id
	Integer userId;
	String userName;
	String userAddr;
	


	}

	
	
	
	
	
	


