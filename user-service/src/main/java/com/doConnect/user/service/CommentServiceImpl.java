package com.doConnect.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doConnect.user.entity.Comment;
import com.doConnect.user.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment addCommentToQuestion(Long questionId, String userEmail, String text) {
        //  USE THE NEW OPTIONAL QUERY METHOD
        Optional<Comment> existingComment = commentRepository
            .findByQuestionIdAndUserEmailAndText(questionId, userEmail, text.trim());
        
        if (existingComment.isPresent()) {
            System.out.println("🚫 DUPLICATE BLOCKED: " + text + " by " + userEmail);
            return existingComment.get();
        }
        
        // No duplicate - create new
        Comment comment = new Comment();
        comment.setQuestionId(questionId);
        comment.setUserEmail(userEmail);
        comment.setText(text.trim());
        Comment saved = commentRepository.save(comment);
        System.out.println("✅ NEW COMMENT SAVED: ID=" + saved.getId());
        return saved;
    }



    @Override
    public List<Comment> getCommentsForQuestion(Long questionId) {
        return commentRepository.findByQuestionIdOrderByCreatedAtDesc(questionId);
    }
}
