package com.example.fitchallenge.dto.traningplandto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPlanResponse {
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

