package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.RewardDTO.*;
import com.example.FIT_Challenge.Entity.Reward;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.RewardRepository;
import com.example.FIT_Challenge.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;

    @Override
    public NotificationResponse createReward(RewardRequest request) {
        Reward reward = Reward.builder()
                .linkImange(request.getLinkImage())
                .name(request.getName())
                .description(request.getDescription())
                .costPoints(request.getCostPoints())
                .stock(request.getStock())
                .externalPartner(request.getExternalPartner())
                .build();
        rewardRepository.save(reward);
        return new NotificationResponse(true, "Reward created successfully", reward);
    }

    @Override
    public NotificationResponse updateReward(Long id, RewardRequest request) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));
        reward.setName(request.getName());
        reward.setDescription(request.getDescription());
        reward.setLinkImange(request.getLinkImage());
        reward.setCostPoints(request.getCostPoints());
        reward.setStock(request.getStock());
        reward.setExternalPartner(request.getExternalPartner());
        rewardRepository.save(reward);
        return new NotificationResponse(true, "Reward updated", reward);
    }

    @Override
    public NotificationResponse deleteReward(Long id) {
        rewardRepository.deleteById(id);
        return new NotificationResponse(true, "Reward deleted");
    }

    @Override
    public NotificationResponse getRewardById(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));
        return new NotificationResponse(true, "Success", reward);
    }

    @Override
    public NotificationResponse getAllRewards() {
        List<Reward> rewards = rewardRepository.findAll();
        return new NotificationResponse(true, "Success", rewards);
    }
}
