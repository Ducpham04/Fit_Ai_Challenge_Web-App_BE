package com.example.fitchallenge.DTO.TraningPlanDTO;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPlanRequestDTO {
    @NotNull(message = "Goal ID is required")
    private Long goalId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel; // BEGINNER, INTERMEDIATE, ADVANCED

    @NotNull(message = "Duration (weeks) is required")
    @Min(value = 1, message = "Duration must be at least 1 week")
    @Max(value = 104, message = "Duration must not exceed 104 weeks")
    private Integer durationWeeks;
}
