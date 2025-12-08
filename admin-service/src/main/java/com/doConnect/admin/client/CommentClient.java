package com.doConnect.admin.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doConnect.admin.dto.Comment;

@FeignClient(name = "DOCONNECT-USER-SERVICE", contextId = "commentClient")
public interface CommentClient {
    @GetMapping("/api/questions/{id}/comments")
    List<Comment> getComments(@PathVariable("id") Long id);
}
