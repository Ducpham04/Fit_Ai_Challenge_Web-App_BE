package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.NutritionPlanDTO.NutritionPlanRequest;
import com.example.fitchallenge.config.NotificationResponse;

public interface NutritionPlanService {

    NotificationResponse createPlan(NutritionPlanRequest request);

    NotificationResponse updatePlan(Long id, NutritionPlanRequest request);

    NotificationResponse deletePlan(Long id);

    NotificationResponse getPlanById(Long id);

    NotificationResponse getAllPlans();

    NotificationResponse getPlansByGoal(Long goalId);
}
