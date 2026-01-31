package com.example.fitchallenge.DTO.TrainingPlanDetailDTO;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TrainingPlanDetailRequest {
    @NotNull(message = "Training plan ID is required")
    private Long trainingPlanId;

    @NotNull(message = "Day number is required")
    @Min(value = 1, message = "Day number must be at least 1")
    @Max(value = 365, message = "Day number must not exceed 365")
    private Integer dayNumber;

    @NotNull(message = "Challenge ID is required")
    private Long challengeId;

    @Min(value = 1, message = "Sets must be at least 1")
    @Max(value = 20, message = "Sets must not exceed 20")
    private Integer sets;

    @Min(value = 1, message = "Reps must be at least 1")
    @Max(value = 1000, message = "Reps must not exceed 1000")
    private Integer reps;

    @Min(value = 1, message = "Duration must be at least 1 second")
    @Max(value = 86400, message = "Duration must not exceed 86400 seconds")
    private Integer duration; // Optional: duration in seconds

    @Min(value = 0, message = "Rest time must be >= 0 seconds")
    @Max(value = 3600, message = "Rest time must not exceed 3600 seconds")
    private Integer restTime; // Optional: rest time in seconds

    @Size(max = 2000, message = "Instructions must not exceed 2000 characters")
    private String instructions; // Optional: exercise instructions
}
