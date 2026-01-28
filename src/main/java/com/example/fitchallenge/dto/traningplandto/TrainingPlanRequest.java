package com.example.fitchallenge.dto.traningplandto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPlanRequest {
    private Long goalId;
    private String title;
    private String description;
    private String difficultyLevel;
    private Integer durationWeeks;
}
