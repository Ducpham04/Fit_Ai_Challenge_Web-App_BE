package com.example.fitchallenge.dto.rewardredemptiondto;

import lombok.Data;

@Data
public class RewardRedemptionRequest {
    private Long userId;
    private Long rewardId;
}
