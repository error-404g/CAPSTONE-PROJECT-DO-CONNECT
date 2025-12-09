package com.company.entity;

import com.company.entity.base.Address;
import com.company.entity.base.Person;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("TEACHER")
public class Teacher extends Person {

	private String subject;
	
	
	public Teacher()
	{}
	public Teacher(String name , Address addr , String subject)
	{
		
		super(name , addr);
		this.subject = subject;
	}
	
}
