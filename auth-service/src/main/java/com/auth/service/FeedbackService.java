package com.auth.service;


import com.auth.model.entity.FeedbackEntity;
import com.auth.repo.FeedbackRepository;
import com.auth.repo.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    public FeedbackEntity saveFeedback(Long userId, String content) {
        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setUser(userRepository.findById(userId).get());
        System.out.println(userRepository.findById(userId).get());
        System.out.println(feedback.getUser().getId());
        feedback.setContent(content);
        return feedbackRepository.save(feedback);
    }

    public List<FeedbackEntity> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}

