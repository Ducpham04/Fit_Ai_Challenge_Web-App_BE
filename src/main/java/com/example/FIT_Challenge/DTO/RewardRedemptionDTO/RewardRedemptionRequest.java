package com.example.FIT_Challenge.DTO.RewardRedemptionDTO;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class RewardRedemptionRequest {
    private Long userId;
    private Long rewardId;
}
