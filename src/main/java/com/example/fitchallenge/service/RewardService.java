package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.RewardDTO.AdminRewardDTO;
import com.example.fitchallenge.config.NotificationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RewardService {
    NotificationResponse createReward(AdminRewardDTO request, MultipartFile file);
    NotificationResponse updateReward(Long id, AdminRewardDTO request, MultipartFile file);
    NotificationResponse deleteReward(Long id);
    NotificationResponse getRewardById(Long id);
    NotificationResponse getAllRewards();
}
