package com.example.FIT_Challenge.service;


import com.example.FIT_Challenge.DTO.TraningPlanDTO.TrainingPlanRequestDTO;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface TrainingPlanService {

    NotificationResponse getAllTrainingPlans();

    NotificationResponse getTrainingPlansByGoalId(Long goalId);

    NotificationResponse getTrainingPlanById(Long tpId);

    NotificationResponse createTrainingPlan(TrainingPlanRequestDTO dto);

    NotificationResponse updateTrainingPlan(Long tpId, TrainingPlanRequestDTO dto);

    NotificationResponse deleteTrainingPlan(Long tpId);
}
