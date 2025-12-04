package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.UserNutritionDTO.UserNutritionRequest;
import com.example.fitchallenge.config.NotificationResponse;

public interface UserNutritionService {
    NotificationResponse createUserNutrition(UserNutritionRequest request);
    NotificationResponse updateUserNutrition(Long id, UserNutritionRequest request);
    NotificationResponse deleteUserNutrition(Long id);
    NotificationResponse getUserNutrition(Long id);
    NotificationResponse getAllUserNutrition();
}
