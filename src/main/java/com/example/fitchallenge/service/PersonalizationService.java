package com.example.fitchallenge.service;

import com.example.fitchallenge.config.NotificationResponse;

public interface PersonalizationService {
    /**
     * Tạo PersonalizedPlanDetail cho user khi bắt đầu training plan
     * Dựa trên Health Profile để chọn template phù hợp và cá nhân hóa
     */
    NotificationResponse createPersonalizedPlanDetails(Long utId);
    
    /**
     * Lấy danh sách bài tập đã cá nhân hóa cho một ngày cụ thể
     * Video URL được lấy từ Challenge entity
     */
    NotificationResponse getPersonalizedDayDetails(Long utId, Integer dayNumber);
    
    /**
     * Lấy bài tập cá nhân hóa cho hôm nay (theo userId và dayNumber)
     * GET /personalized/today?userId=123&dayNumber=5
     */
    NotificationResponse getTodayPersonalizedWorkout(Long userId, Integer dayNumber);
    
    /**
     * Lấy tất cả PersonalizedPlanDetail của user (cho admin)
     */
    NotificationResponse getAllPersonalizedPlanDetails(Long userId);
    
    /**
     * Admin cập nhật PersonalizedPlanDetail của user
     */
    NotificationResponse updatePersonalizedPlanDetail(Long ppdId, com.example.fitchallenge.dto.personalizedplandetaildto.PersonalizedPlanDetailResponse request);
}


