package com.example.fitchallenge.DTO.RewardRedemptionDTO;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class RewardRedemptionResponse {
    private Long id;
    private String userName;
    private String rewardName;
    private String status;
    private ZonedDateTime createdAt;
    private ZonedDateTime fulfilledAt;
}
