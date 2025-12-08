package com.doConnect.user.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.doConnect.user.entity.ChatMessageEntity;
import com.doConnect.user.entity.User;
import com.doConnect.user.repository.ChatMessageRepository;
import com.doConnect.user.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository repo;  
    
    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatMessageRepository repo, 
                          UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.repo = repo;
        this.userRepository = userRepository;
    }

    //KEEP DTO - Frontend uses this!
    public static class ChatMessage {
        private Long id;
        private String senderEmail;
        private String receiverEmail;
        private String senderUsername;
        private String content;
        private String timestamp;
        private String chatRoomId;
        
        // ALL getters/setters 
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getSenderEmail() { return senderEmail; }
        public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
        public String getReceiverEmail() { return receiverEmail; }
        public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }
        public String getSenderUsername() { return senderUsername; }
        public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getChatRoomId() { return chatRoomId; }
        public void setChatRoomId(String chatRoomId) { this.chatRoomId = chatRoomId; }
    }

    //@MessageMapping + ChatMessage DTO (NOT Entity!)
    @MessageMapping("/chat.sendPrivate")
    public void sendPrivate(@Payload ChatMessage message) {  // ✅ DTO!
        System.out.println("🔥 CHAT CONTROLLER HIT: " + message.getContent()); 

        // ✅ 1. Save to DB as Entity
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSenderEmail(message.getSenderEmail());
        entity.setReceiverEmail(message.getReceiverEmail());
        entity.setContent(message.getContent());
        entity.setTimestamp(LocalDateTime.now());  // ✅ Entity timestamp
        ChatMessageEntity saved = repo.save(entity);
        
        // ✅ 2. Update DTO with DB ID
        message.setId(saved.getId());
        
        // ✅ 3. Lookup username
        User senderUser = userRepository.findByEmail(message.getSenderEmail()).orElse(null);
        message.setSenderUsername(senderUser != null ? senderUser.getUsername() : message.getSenderEmail());
        
        // ✅ 4. Timestamp as String for frontend
        message.setTimestamp(LocalDateTime.now().toString());
        
        // ✅ 5. Broadcast to ALL
        messagingTemplate.convertAndSend("/topic/chat", message);
        messagingTemplate.convertAndSend("/topic/admin-chat", message);
    }

    // History - Convert Entity → DTO for frontend
    @GetMapping("/chat/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory() {
        try {
            List<ChatMessageEntity> entities = repo.findTop50ByOrderByTimestampDesc();
            List<ChatMessage> history = new ArrayList<>();
            
            for (ChatMessageEntity entity : entities) {
                ChatMessage dto = new ChatMessage();
                dto.setId(entity.getId());
                dto.setSenderEmail(entity.getSenderEmail());
                dto.setReceiverEmail(entity.getReceiverEmail());
                dto.setContent(entity.getContent());
                dto.setTimestamp(entity.getTimestamp().toString());
                history.add(dto);
            }
            
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            System.out.println("❌ History error: " + e.getMessage());
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
}