package com.doConnect.admin.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.doConnect.admin.dto.User;

@FeignClient(name = "DOCONNECT-USER-SERVICE", contextId = "userClient")
public interface UserClient {

    //Correct endpoint
    @GetMapping("/api/users")  
    List<User> getAllUsers();
    
    @GetMapping("/api/users/email/{email}")
    User findByEmail(@PathVariable("email") String email);
    
    @PostMapping("/api/users/admin/users/{id}/block")
    String blockUser(@PathVariable("id") Long id);

    @PostMapping("/api/users/admin/users/{id}/unblock")
    String unblockUser(@PathVariable("id") Long id);
}
