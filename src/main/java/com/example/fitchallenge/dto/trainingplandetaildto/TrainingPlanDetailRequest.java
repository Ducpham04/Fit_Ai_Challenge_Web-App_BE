package com.example.fitchallenge.dto.trainingplandetaildto;



import lombok.Data;

@Data
public class TrainingPlanDetailRequest {
    private Long trainingPlanId;
    private Integer dayNumber;
    private Long challengeId;
    private Integer sets;
    private Integer reps;
    private Integer duration; // Optional: duration in seconds
    private Integer restTime; // Optional: rest time in seconds
    private String instructions; // Optional: exercise instructions
}
