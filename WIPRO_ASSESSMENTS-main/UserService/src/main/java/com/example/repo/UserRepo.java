package com.example.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.beans.User;

@Repository
public interface UserRepo extends CrudRepository<User , Integer> {

}
