package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.RewardDTO.AdminRewardDTO;
import com.example.FIT_Challenge.DTO.RewardDTO.RewardRequest;
import com.example.FIT_Challenge.DTO.RewardDTO.RewardResponse;
import com.example.FIT_Challenge.config.NotificationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RewardService {
    NotificationResponse createReward(AdminRewardDTO request, MultipartFile file);
    NotificationResponse updateReward(Long id, AdminRewardDTO request, MultipartFile file);
    NotificationResponse deleteReward(Long id);
    NotificationResponse getRewardById(Long id);
    NotificationResponse getAllRewards();
}
