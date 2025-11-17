package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.NutritionPlanDTO.NutritionPlanRequest;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface NutritionPlanService {

    NotificationResponse createPlan(NutritionPlanRequest request);

    NotificationResponse updatePlan(Long id, NutritionPlanRequest request);

    NotificationResponse deletePlan(Long id);

    NotificationResponse getPlanById(Long id);

    NotificationResponse getAllPlans();

    NotificationResponse getPlansByGoal(Long goalId);
}
