package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.RewardRedemptionDTO.RewardRedemptionRequest;
import com.example.FIT_Challenge.DTO.RewardRedemptionDTO.RewardRedemptionResponse;
import com.example.FIT_Challenge.Entity.Reward;
import com.example.FIT_Challenge.Entity.RewardRedemption;
import com.example.FIT_Challenge.Entity.User;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.RewardRedemptionRepository;
import com.example.FIT_Challenge.repository.RewardRepository;
import com.example.FIT_Challenge.repository.User.UserRepository;
import com.example.FIT_Challenge.service.RewardRedemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardRedemptionServiceImpl implements RewardRedemptionService {

    private final RewardRedemptionRepository rewardRedemptionRepository;
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;

    // Chuyển entity -> DTO
    private RewardRedemptionResponse toResponse(RewardRedemption redemption) {
        RewardRedemptionResponse dto = new RewardRedemptionResponse();
        dto.setId(redemption.getId());
        dto.setUserName(redemption.getUser().getUserName());
        dto.setRewardName(redemption.getReward().getName());
        dto.setStatus(redemption.getStatus());
        dto.setCreatedAt(redemption.getCreatedAt());
        dto.setFulfilledAt(redemption.getFulfilledAt());
        return dto;
    }

    @Override
    public NotificationResponse redeemReward(RewardRedemptionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Reward reward = rewardRepository.findById(request.getRewardId())
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        RewardRedemption redemption = new RewardRedemption();
        redemption.setUser(user);
        redemption.setReward(reward);
        redemption.setStatus("pending");
        redemption.setCreatedAt(ZonedDateTime.now());
        rewardRedemptionRepository.save(redemption);

        RewardRedemptionResponse response = toResponse(redemption);
        return new NotificationResponse(true, "Reward redemption created", response);
    }

    @Override
    public NotificationResponse getAllRedemptions() {
        List<RewardRedemptionResponse> responseList = rewardRedemptionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new NotificationResponse(true, "All redemptions retrieved", responseList);
    }

    @Override
    public NotificationResponse updateStatus(Long redemptionId, String status) {
        RewardRedemption redemption = rewardRedemptionRepository.findById(redemptionId)
                .orElseThrow(() -> new RuntimeException("Redemption not found"));

        redemption.setStatus(status);
        if ("fulfilled".equalsIgnoreCase(status)) {
            redemption.setFulfilledAt(ZonedDateTime.now());
        }
        rewardRedemptionRepository.save(redemption);

        RewardRedemptionResponse response = toResponse(redemption);
        return new NotificationResponse(true, "Status updated", response);
    }
}
