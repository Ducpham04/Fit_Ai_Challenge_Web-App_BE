package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.UserChallengeDTO.UserChallengeDTO;
import com.example.fitchallenge.config.NotificationResponse;

public interface UserChallengeService {
    NotificationResponse getAll();
    NotificationResponse getById(Long id);
    NotificationResponse create(UserChallengeDTO dto);
    NotificationResponse update(Long id, UserChallengeDTO dto);
    NotificationResponse delete(Long id);
    
    /**
     * Đánh dấu challenge đã hoàn thành
     * Set status = "success" và completedAt = now()
     */
    NotificationResponse completeChallenge(Long id, Long userId);
}
