package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.RewardDTO.RewardRequest;
import com.example.FIT_Challenge.DTO.RewardDTO.RewardResponse;
import com.example.FIT_Challenge.config.NotificationResponse;
import java.util.List;

public interface RewardService {
    NotificationResponse createReward(RewardRequest request);
    NotificationResponse updateReward(Long id, RewardRequest request);
    NotificationResponse deleteReward(Long id);
    NotificationResponse getRewardById(Long id);
    NotificationResponse getAllRewards();
}
