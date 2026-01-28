package com.example.fitchallenge.dto.mealdto;

import com.example.fitchallenge.dto.mealfooddto.MealFoodResponse;
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
