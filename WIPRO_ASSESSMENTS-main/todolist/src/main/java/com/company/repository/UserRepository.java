package com.company.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.company.model.User;

@Repository
public class UserRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public boolean findUser(String username, String password) {
        try {
            String sql = "SELECT * FROM user WHERE username=? AND password=?";
            User u = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username, password);
            return u != null;  // If a user is found, return true
        } catch (EmptyResultDataAccessException e) {
            return false;  // No user found, return false instead of throwing an exception
        }
    }
    
    
    
    
    
    
    // Check if username already exists
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username=?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;
    }

    // Insert new user into the database
    public int saveUser(User user) {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

	
}
