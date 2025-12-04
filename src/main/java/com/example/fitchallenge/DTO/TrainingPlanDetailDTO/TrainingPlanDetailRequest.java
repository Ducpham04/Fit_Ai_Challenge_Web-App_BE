package com.example.fitchallenge.DTO.TrainingPlanDetailDTO;



import lombok.Data;

@Data
public class TrainingPlanDetailRequest {
    private Long trainingPlanId;
    private Integer dayNumber;
    private Long challengeId;
    private Integer sets;
    private Integer reps;
}
