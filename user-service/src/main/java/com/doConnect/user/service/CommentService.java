package com.doConnect.user.service;

import java.util.List;

import com.doConnect.user.entity.Comment;

public interface CommentService {
    Comment addCommentToQuestion(Long questionId, String userEmail, String text);
    List<Comment> getCommentsForQuestion(Long questionId);
    
}
