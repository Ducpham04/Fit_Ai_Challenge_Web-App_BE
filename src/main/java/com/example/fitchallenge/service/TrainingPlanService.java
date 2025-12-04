package com.example.fitchallenge.service;


import com.example.fitchallenge.DTO.TraningPlanDTO.TrainingPlanRequestDTO;
import com.example.fitchallenge.config.NotificationResponse;

public interface TrainingPlanService {

    NotificationResponse getAllTrainingPlans();

    NotificationResponse getTrainingPlansByGoalId(Long goalId);

    NotificationResponse getTrainingPlanById(Long tpId);

    NotificationResponse createTrainingPlan(TrainingPlanRequestDTO dto);

    NotificationResponse updateTrainingPlan(Long tpId, TrainingPlanRequestDTO dto);

    NotificationResponse deleteTrainingPlan(Long tpId);
}
