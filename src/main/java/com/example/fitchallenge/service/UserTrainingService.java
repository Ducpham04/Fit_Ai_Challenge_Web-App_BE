package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.fitchallenge.config.NotificationResponse;

public interface UserTrainingService {
    NotificationResponse getUserTrainingDetails(Long userId);
    NotificationResponse createUserTraining(UserRequestDTO res);
    NotificationResponse startTrainingPlan(Long trainingPlanId, Long userId, String startDate);
    NotificationResponse getUsersFollowingTrainingPlan(Long trainingPlanId);
}
