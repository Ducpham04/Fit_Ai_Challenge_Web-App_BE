package com.example.fitchallenge.service;

import com.example.fitchallenge.config.NotificationResponse;

public interface PersonalizationService {
    /**
     * Tạo PersonalizedPlanDetail cho user khi bắt đầu training plan
     */
    NotificationResponse createPersonalizedPlanDetails(Long utId);
    
    /**
     * Lấy danh sách bài tập đã cá nhân hóa cho một ngày cụ thể
     */
    NotificationResponse getPersonalizedDayDetails(Long utId, Integer dayNumber);
}

