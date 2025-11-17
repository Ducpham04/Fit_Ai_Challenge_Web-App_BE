package com.example.FIT_Challenge.DTO.MealDTO;

import lombok.Data;

@Data
public class MealResponse {
    private Long mealId;
    private Long nutritionPlanId;
    private String nutritionPlanTitle; // tên kế hoạch
    private String mealType;
    private String name;
    private String description;
    private Integer caloriesEstimate;
}
