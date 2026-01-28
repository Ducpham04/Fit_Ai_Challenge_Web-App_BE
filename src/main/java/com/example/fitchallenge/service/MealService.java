package com.example.fitchallenge.service;

import com.example.fitchallenge.dto.mealdto.MealRequest;
import com.example.fitchallenge.dto.mealdto.MealResponse;
import com.example.fitchallenge.config.NotificationResponse;

import java.util.List;

public interface MealService {

    NotificationResponse createMeal(MealRequest request);

    NotificationResponse updateMeal(Long id, MealRequest request);

    NotificationResponse deleteMeal(Long id);

    NotificationResponse getMealById(Long id);

    NotificationResponse getAllMeals();

    NotificationResponse getMealsByPlan(Long planId);
    List<MealResponse> getMealByPlanId(Long planId);
}
