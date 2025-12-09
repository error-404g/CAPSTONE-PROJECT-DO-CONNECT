package com.example.validation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.validation.model.User;

@Repository
public interface UserRepository extends JpaRepository<User , Long>{

}




// Repository is use to persist the data into database using hibernate and jdbc
// Repository are of two types  :  CrudRepository (Crud operations) 
// and JpaRepository is for (Crud + PaginationAndSorting)