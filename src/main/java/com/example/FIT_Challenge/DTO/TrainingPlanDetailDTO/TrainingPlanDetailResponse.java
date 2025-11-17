package com.example.FIT_Challenge.DTO.TrainingPlanDetailDTO;

import lombok.Data;

@Data
public class TrainingPlanDetailResponse {
    private Long tpdId;
    private Long trainingPlanId;
    private String trainingPlanTitle;
    private Integer dayNumber;
    private Long challengeId;
    private String challengeName;
    private Integer sets;
    private Integer reps;
}
