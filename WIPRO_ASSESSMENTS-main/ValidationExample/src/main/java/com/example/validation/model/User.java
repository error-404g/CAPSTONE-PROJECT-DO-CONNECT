package com.example.validation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="userdata")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message="Name is mandatory to provide")
	private String name;
	@Email(message = " Invalid format of an email")
	private String email;
	@Min(value=18, message="Age should be 18 as a min")
	@Max(value=30 ,message ="Age should be 30 as a max")
	private int age;
}

// validation  -- frontend  // Back end  on attributes  // Database 
