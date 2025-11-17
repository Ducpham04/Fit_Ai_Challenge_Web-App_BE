package com.example.FIT_Challenge.DTO.NutritionPlanDTO;

import lombok.Data;

@Data
public class NutritionPlanRequest {
    private Long goalId;
    private String title;
    private String description;
    private Integer caloriesPerDay;
    private Integer proteinG;
    private Integer carbsG;
    private Integer fatG;
    private String status;
}