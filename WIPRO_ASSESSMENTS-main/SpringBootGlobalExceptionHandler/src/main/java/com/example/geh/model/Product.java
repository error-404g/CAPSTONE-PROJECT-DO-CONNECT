package com.example.geh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
	public Product(Long id, String name, int price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
	private Long id;
	private String name;
	private double price;

}
