package com.example.fitchallenge.DTO.RewardRedemptionDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RewardRedemptionRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Reward ID is required")
    private Long rewardId;
}
