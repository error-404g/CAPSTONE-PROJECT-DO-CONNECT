package com.doConnect.user.controller;

import com.doConnect.user.entity.ChatMessageEntity;
import com.doConnect.user.entity.User;
import com.doConnect.user.repository.ChatMessageRepository;
import com.doConnect.user.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatCrudController {
	
    private final ChatMessageRepository repo;
	
    private final SimpMessagingTemplate messagingTemplate;
	
    private final UserRepository userRepository;  

    public ChatCrudController(ChatMessageRepository repo,
                              SimpMessagingTemplate messagingTemplate,
                              UserRepository userRepository) {  
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id, @RequestBody Map<String, String> body) {
        System.out.println("🖊️ EDIT: id=" + id);
        ChatMessageEntity msg = repo.findById(id).orElseThrow();
        msg.setContent(body.get("content"));
        repo.save(msg);
        broadcastUpdate(msg, "EDIT");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        System.out.println("🗑️ DELETE: id=" + id);
        ChatMessageEntity msg = repo.findById(id).orElseThrow();
        msg.setDeleted(true);
        repo.save(msg);
        broadcastUpdate(msg, "DELETE");
        return ResponseEntity.ok().build();
    }


    private void broadcastUpdate(ChatMessageEntity msg, String type) {
    	String senderUsername = userRepository.findByEmail(msg.getSenderEmail())
    	        .map(User::getUsername)
    	        .orElse(msg.getSenderEmail());
        Map<String, Object> payload = Map.of(
            "id", msg.getId(),
            "senderEmail", msg.getSenderEmail(),
            "senderUsername", senderUsername,
            "receiverEmail", msg.getReceiverEmail(),
            "content", msg.getContent(),
            "deleted", msg.isDeleted(),
            "type", type
        );
        
        // Direct destinations (not convertAndSendToUser)
        messagingTemplate.convertAndSend("/topic/chat", payload);
        
    }
}
