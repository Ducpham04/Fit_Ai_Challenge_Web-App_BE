package com.example.fitchallenge.service;

import com.example.fitchallenge.dto.rewardredemptiondto.RewardRedemptionRequest;
import com.example.fitchallenge.config.NotificationResponse;

public interface RewardRedemptionService {
    NotificationResponse redeemReward(RewardRedemptionRequest request);
    NotificationResponse getAllRedemptions();
    NotificationResponse updateStatus(Long redemptionId, String status);
}
