package com.example.fitchallenge.dto.rewardto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class RewardResponse {
    private Long rewardId;
    private String linkImage;
    private String name;
    private String description;
    private Integer costPoints;
    private Integer stock;
    private String externalPartner;
    private OffsetDateTime createdAt;
}
