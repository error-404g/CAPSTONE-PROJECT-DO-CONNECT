package com.company.entity.base;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="dtype")
public abstract class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Embedded
	private Address address;

	public Person(String name, Address address) {
		super();
		this.name = name;
		this.address = address;
	}
	

}
