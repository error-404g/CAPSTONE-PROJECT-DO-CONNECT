package com.doConnect.admin.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.doConnect.admin.dto.User;

@FeignClient(name = "DOCONNECT-USER-SERVICE", contextId = "adminUserClient", path = "/api/admin/users")
public interface AdminUserClient {

	    @GetMapping
	    List<User> getAdmins();

	    @PostMapping
	    User createAdmin(@RequestBody User user);

	    @PutMapping("/{id}")
	    User updateAdmin(@PathVariable Long id, @RequestBody User user);

	    @DeleteMapping("/{id}")
	    void deleteAdmin(@PathVariable Long id);
    
    
}

