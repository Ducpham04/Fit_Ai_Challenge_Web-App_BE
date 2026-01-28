package com.example.fitchallenge.service;



import com.example.fitchallenge.dto.mealfooddto.MealFoodRequest;
import com.example.fitchallenge.config.NotificationResponse;

public interface MealFoodService {
    NotificationResponse createMealFood(MealFoodRequest request);
    NotificationResponse updateMealFood(Long id, MealFoodRequest request);
    NotificationResponse deleteMealFood(Long id);
}
