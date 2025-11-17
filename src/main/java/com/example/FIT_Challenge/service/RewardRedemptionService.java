package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.RewardRedemptionDTO.RewardRedemptionRequest;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface RewardRedemptionService {
    NotificationResponse redeemReward(RewardRedemptionRequest request);
    NotificationResponse getAllRedemptions();
    NotificationResponse updateStatus(Long redemptionId, String status);
}
