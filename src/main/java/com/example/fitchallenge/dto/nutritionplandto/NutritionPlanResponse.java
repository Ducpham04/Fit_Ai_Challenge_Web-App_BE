package com.example.fitchallenge.dto.nutritionplandto;


import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class NutritionPlanResponse {
    private Long planId;
    private Long goalId;
    private String goalName; // optional
    private String title;
    private String description;
    private Integer caloriesPerDay;
    private Integer proteinG;
    private Integer carbsG;
    private Integer fatG;
    private String status;
    private OffsetDateTime createdAt;
}