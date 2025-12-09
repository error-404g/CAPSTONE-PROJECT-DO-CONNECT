package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Product;
import com.example.repository.ProductRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Front end
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<Product>> getAllProducts() {
        System.out.println("ProductController: Authorized USER accessed products");
        return ResponseEntity.ok(productRepository.findAll());
}
}

//  filter class will execute before the servlet 