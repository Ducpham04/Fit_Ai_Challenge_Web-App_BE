package com.example.FIT_Challenge.DTO.RewardDTO;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class RewardRequest {
    private String linkImage;
    private String name;
    private String description;
    private Integer costPoints;
    private Integer stock;
    private String externalPartner;
}
