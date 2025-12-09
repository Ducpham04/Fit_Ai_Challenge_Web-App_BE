package com.example.fitchallenge.DTO.MealDTO;

import com.example.fitchallenge.DTO.MealFoodDTO.MealFoodResponse;
import lombok.Data;

import java.util.List;

@Data
public class MealResponse {
    private Long mealId;
    private Long nutritionPlanId;
    private String mealType;
    private String name;
    private Integer dayNumber;
    private String description;
    private Integer caloriesEstimate;
    private List<MealFoodResponse> foods ;
}
