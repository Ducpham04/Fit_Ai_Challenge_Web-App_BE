package com.example.fitchallenge.DTO.TraningPlanDTO;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPlanResponseDTO {
    private Long tpId;
    private Long goalId;
    private String goalName;
    private String  linkImage;
    private String title;
    private String description;
    private String difficultyLevel;
    private Integer durationWeeks;
    private String createdAt;
}

