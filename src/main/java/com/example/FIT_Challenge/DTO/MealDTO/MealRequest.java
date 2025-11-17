package com.example.FIT_Challenge.DTO.MealDTO;

import lombok.Data;

@Data
public class MealRequest {
    private Long nutritionPlanId; // liên kết đến NutritionPlan
    private String mealType;
    private String name;
    private String description;
    private Integer caloriesEstimate;
}
