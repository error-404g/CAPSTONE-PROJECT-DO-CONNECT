package com.doConnect.user.repository;

import com.doConnect.user.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTitleContainingIgnoreCase(String title);
    List<Question> findByContentContainingIgnoreCase(String content);
    List<Question> findByApprovedTrue();
    

    // search in both title and content with one keyword
    List<Question> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content);

    // find resolved questions
    List<Question> findByResolvedTrue();

    // find unresolved (for “pending/completed” views)
    List<Question> findByResolvedFalse();
    
    List<Question> findByUserEmail(String userEmail);
    
    Page<Question> findByUserEmail(String userEmail, Pageable pageable);
    
    Page<Question> findAll(Pageable pageable);

    Page<Question> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
        String title, String content, Pageable pageable);
    
 // bell notifications
    Page<Question> findByUserEmailAndApprovedTrueOrderByCreatedAtDesc(String userEmail, Pageable pageable);
    
    Page<Question> findByApprovedTrue(Pageable pageable);
    Page<Question> findByApprovedFalse(Pageable pageable);



}
