package com.example.geh.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.geh.exception.ResourceNotFoundException;
import com.example.geh.model.Product;

@Service
public class ProductService {
	private static final Logger Logger = LoggerFactory.getLogger(ProductService.class);

	private final Map<Long ,Product> productRepo = new HashMap();
	
	public ProductService()
	{
		productRepo.put(1L, new Product(1L,"Laptop" , 60000));
		productRepo.put(2L, new Product(2L,"Keyboard" , 1000));
		
	}
	
	public Product getProductById(Long id) throws ResourceNotFoundException
	{
		Logger.info("Fetching the product with id : {} " , id );
		
		if (!productRepo.containsKey(id))
		{
			throw new ResourceNotFoundException("Product Not Found with this id :" + id);
		}
		return productRepo.get(id); // You are retreiving on the basis of key
	}
	
	public Collection<Product> getAllProducts()
	{
		
		Logger.info("Fetching All Products ");
		return productRepo.values();
		
	}
	
}
