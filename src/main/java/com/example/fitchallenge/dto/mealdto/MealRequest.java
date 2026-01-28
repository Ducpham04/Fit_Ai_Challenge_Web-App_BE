package com.example.fitchallenge.dto.mealdto;

import com.example.fitchallenge.dto.mealfooddto.MealFoodRequest;
import lombok.Data;

import java.util.List;

@Data
public class MealRequest {
    private Long nutritionPlanId; // liên kết đến NutritionPlan
    private String mealType;
    private String name;
    private Integer dayNumber;
    private String description;
    private Integer caloriesEstimate;
    private List<MealFoodRequest> foods ;
}
