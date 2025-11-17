package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.UserNutritionDTO.UserNutritionRequest;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface UserNutritionService {
    NotificationResponse createUserNutrition(UserNutritionRequest request);
    NotificationResponse updateUserNutrition(Long id, UserNutritionRequest request);
    NotificationResponse deleteUserNutrition(Long id);
    NotificationResponse getUserNutrition(Long id);
    NotificationResponse getAllUserNutrition();
}
