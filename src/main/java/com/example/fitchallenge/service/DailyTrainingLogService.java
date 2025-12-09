package com.example.fitchallenge.service;

import com.example.fitchallenge.config.NotificationResponse;

public interface DailyTrainingLogService {
    /**
     * Lấy tất cả daily training logs của user trong một training plan
     * Kết hợp với challenge information từ template
     */
    NotificationResponse getDailyTrainingLogsByUserAndPlan(Long userId, Long trainingPlanId);
    
    /**
     * Lấy daily training logs của user trong một training plan theo day number
     */
    NotificationResponse getDailyTrainingLogsByUserAndPlanAndDay(Long userId, Long trainingPlanId, Integer dayNumber);
    
    /**
     * Tạo hoặc cập nhật daily training log
     */
    NotificationResponse createOrUpdateDailyTrainingLog(
            Long userId, 
            Long trainingPlanId, 
            Integer dayNumber, 
            Long challengeId, 
            String status,
            Integer repsCompleted,
            Integer setsCompleted,
            Integer score,
            Double confidence,
            Integer actualDurationMinutes);
}

