package com.example.fitchallenge.dto.rewardto;

import lombok.Data;

@Data
public class RewardRequest {
    private String linkImage;
    private String name;
    private String description;
    private Integer costPoints;
    private Integer stock;
    private String externalPartner;
}
