package com.example.FIT_Challenge.service;



import com.example.FIT_Challenge.DTO.MealFoodDTO.MealFoodRequest;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface MealFoodService {
    NotificationResponse createMealFood(MealFoodRequest request);
    NotificationResponse updateMealFood(Long id, MealFoodRequest request);
    NotificationResponse deleteMealFood(Long id);
}
