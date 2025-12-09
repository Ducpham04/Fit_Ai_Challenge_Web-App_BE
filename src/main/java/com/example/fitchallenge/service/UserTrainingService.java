package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.fitchallenge.config.NotificationResponse;

public interface UserTrainingService {
    NotificationResponse getUserTrainingDetails(Long userId);
    NotificationResponse createUserTraining(UserRequestDTO res);
    NotificationResponse startTrainingPlan(Long trainingPlanId, Long userId, String startDate);
    NotificationResponse getUsersFollowingTrainingPlan(Long trainingPlanId);
    
    /**
     * Xóa training plan của user (chỉ user sở hữu mới có thể xóa)
     * Xóa cascade: PersonalizedPlanDetail, DailyTrainingLog
     */
    NotificationResponse deleteUserTraining(Long utId, Long userId);
}
