package com.example.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.beans.Userr;

@Repository
public interface UserRepo extends CrudRepository<Userr , Integer> {

}
