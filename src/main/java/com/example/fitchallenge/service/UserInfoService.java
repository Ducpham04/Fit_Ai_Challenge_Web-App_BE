package com.example.fitchallenge.service;

import com.example.fitchallenge.dto.UserInfoDTO;
import com.example.fitchallenge.config.NotificationResponse;

public interface UserInfoService {
    /**
     * Lấy thông tin user (body info) của user hiện tại
     */
    NotificationResponse getUserInfo(Long userId);
    
    /**
     * Cập nhật thông tin body của user
     * Tự động tính BMI, BMR, recommended calories
     */
    NotificationResponse updateUserInfo(Long userId, UserInfoDTO dto);
}





