package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.TraningPlanDTO.TrainingPlanRequestDTO;
import com.example.fitchallenge.config.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainingPlanService {

    NotificationResponse getAllTrainingPlans();

    NotificationResponse getTrainingPlansByGoalId(Long goalId);

    NotificationResponse getTrainingPlanById(Long tpId);

    NotificationResponse createTrainingPlan(TrainingPlanRequestDTO dto);

    NotificationResponse updateTrainingPlan(Long tpId, TrainingPlanRequestDTO dto);

    NotificationResponse deleteTrainingPlan(Long tpId);
    
    // Duplicate plan
    NotificationResponse duplicateTrainingPlan(Long tpId);
    
    // Publish/Unpublish plan
    NotificationResponse publishTrainingPlan(Long tpId, boolean publish);
    
    // New methods for user-facing APIs
    Page<com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO> getAllTrainingPlansForUser(String difficulty, String status, Long goalId, Pageable pageable);
    com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO getTrainingPlanByIdForUser(Long tpId);
}
