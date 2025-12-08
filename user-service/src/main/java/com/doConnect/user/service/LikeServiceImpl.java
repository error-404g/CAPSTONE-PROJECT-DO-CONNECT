package com.doConnect.user.service;

import com.doConnect.user.entity.Like;
import com.doConnect.user.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Override
    public void likeQuestion(Long questionId, String userEmail) {
        boolean alreadyLiked = likeRepository.existsByQuestionIdAndUserEmail(questionId, userEmail);
        if (!alreadyLiked) {
            Like like = new Like(questionId, userEmail);
            likeRepository.save(like);
        }
    }

    @Override
    public long getQuestionLikeCount(Long questionId) {
        return likeRepository.countByQuestionId(questionId);
    }

    @Override
    public void likeAnswer(Long answerId, String userEmail) {
        boolean alreadyLiked = likeRepository.existsByAnswerIdAndUserEmail(answerId, userEmail);
        if (!alreadyLiked) {
            Like like = new Like();
            like.setAnswerId(answerId);
            like.setUserEmail(userEmail);
            likeRepository.save(like);
        }
    }

    @Override
    public long getAnswerLikeCount(Long answerId) {
        return likeRepository.countByAnswerId(answerId);
    }
}
