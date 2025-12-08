package com.doConnect.user.service;

public interface LikeService {
    void likeQuestion(Long questionId, String userEmail);
    long getQuestionLikeCount(Long questionId);
    
   
    void likeAnswer(Long answerId, String userEmail);
    long getAnswerLikeCount(Long answerId);
}
