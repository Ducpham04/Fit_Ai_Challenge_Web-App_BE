package com.example.fitchallenge.service;


import com.example.fitchallenge.DTO.TrainingPlanDetailDTO.TrainingPlanDetailRequest;
import com.example.fitchallenge.config.NotificationResponse;

public interface TrainingPlanDetailService {
    NotificationResponse createDetail(TrainingPlanDetailRequest dto);
    NotificationResponse updateDetail(Long id, TrainingPlanDetailRequest dto);
    NotificationResponse deleteDetail(Long id);
    NotificationResponse getAllDetails();
    NotificationResponse getDetailsByPlanId(Long planId);
    NotificationResponse getDetailById(Long id);
}
