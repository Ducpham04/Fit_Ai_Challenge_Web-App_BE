package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.MealDTO.MealRequest;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface MealService {

    NotificationResponse createMeal(MealRequest request);

    NotificationResponse updateMeal(Long id, MealRequest request);

    NotificationResponse deleteMeal(Long id);

    NotificationResponse getMealById(Long id);

    NotificationResponse getAllMeals();

    NotificationResponse getMealsByPlan(Long planId);
}
