package com.company.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.company.entities.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product , Long> {

}
