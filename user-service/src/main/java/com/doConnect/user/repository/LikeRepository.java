package com.doConnect.user.repository;

import com.doConnect.user.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    boolean existsByQuestionIdAndUserEmail(Long questionId, String userEmail);
    long countByQuestionId(Long questionId);
    
    // For future answer likes
    boolean existsByAnswerIdAndUserEmail(Long answerId, String userEmail);
    long countByAnswerId(Long answerId);
}
