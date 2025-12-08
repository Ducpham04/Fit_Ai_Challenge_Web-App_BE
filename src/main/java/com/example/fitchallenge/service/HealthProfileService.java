package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.HealthProfileDTO.HealthProfileRequest;
import com.example.fitchallenge.DTO.HealthProfileDTO.HealthProfileResponse;
import com.example.fitchallenge.config.NotificationResponse;

public interface HealthProfileService {
    
    /**
     * Tạo hoặc cập nhật Health Profile
     * Tự động tính toán BMI, BMR, TDEE, Body Fat, Lean Body Mass
     */
    NotificationResponse createOrUpdateHealthProfile(Long userId, HealthProfileRequest request);
    
    /**
     * Lấy Health Profile của user
     */
    NotificationResponse getHealthProfile(Long userId);
    
    /**
     * Gợi ý Training Plans dựa trên Health Profile
     * Dựa trên: body fat, mục tiêu, mức độ vận động, thiết bị, sức khỏe
     */
    NotificationResponse getRecommendedTrainingPlans(Long userId);
    
    /**
     * Sinh Personalized Plan Detail tự động
     * Tạo training plan detail được cá nhân hóa cho user
     */
    NotificationResponse generatePersonalizedPlanDetail(Long userId, Long trainingPlanId);
}


