package com.company.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.entities.Product;
import com.company.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/products")
public class ProductController {
	
	private final ProductRepository  repo;
	
	public ProductController(ProductRepository repo)
	{
		this.repo= repo;	}
	
	@GetMapping
	public Flux<Product> getAll()
	{
		return repo.findAll(); // It's emitting multiple items
	}
	
	@GetMapping("/{id}")
	public Mono<Product> getById(@PathVariable Long id)
	{
		return repo.findById(id); // It's emitting single items
	}
	
	@PostMapping("/add")
	public Mono<Product> create(@RequestBody Product product)
	{
		return repo.save(product); // here it is a single object which is a single item
	}
	
	@DeleteMapping("/{id}")
	public Mono<Void> deleteById(@PathVariable Long id)
	{
		return repo.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public Mono<Product> update(@PathVariable Long id , @RequestBody Product product)
	{
		return repo.findById(id)
				.flatMap(existing -> {
					existing.setName(product.getName());
					existing.setPrice(product.getPrice());
					return repo.save(existing);
					
				});
		
	}

}
