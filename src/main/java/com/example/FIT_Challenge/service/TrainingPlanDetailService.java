package com.example.FIT_Challenge.service;


import com.example.FIT_Challenge.DTO.TrainingPlanDetailDTO.TrainingPlanDetailRequest;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface TrainingPlanDetailService {
    NotificationResponse createDetail(TrainingPlanDetailRequest dto);
    NotificationResponse updateDetail(Long id, TrainingPlanDetailRequest dto);
    NotificationResponse deleteDetail(Long id);
    NotificationResponse getAllDetails();
    NotificationResponse getDetailsByPlanId(Long planId);
}
