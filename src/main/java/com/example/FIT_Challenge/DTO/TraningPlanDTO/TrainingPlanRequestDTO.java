package com.example.FIT_Challenge.DTO.TraningPlanDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPlanRequestDTO {
    private Long goalId;
    private String title;
    private String description;
    private String difficultyLevel;
    private Integer durationWeeks;
}
