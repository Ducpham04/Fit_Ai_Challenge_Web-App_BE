package com.example.fitchallenge.DTO.MealDTO;

import com.example.fitchallenge.DTO.MealFoodDTO.MealFoodRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class MealRequest {
    @NotNull(message = "Nutrition plan ID is required")
    private Long nutritionPlanId; // liên kết đến NutritionPlan

    @NotBlank(message = "Meal type is required")
    private String mealType; // breakfast, lunch, dinner, snack

    @NotBlank(message = "Meal name is required")
    @Size(max = 255, message = "Meal name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Day number is required")
    @Min(value = 1, message = "Day number must be at least 1")
    @Max(value = 365, message = "Day number must not exceed 365")
    private Integer dayNumber;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Calories estimate must be greater than or equal to 0")
    private Integer caloriesEstimate;

    @NotEmpty(message = "Meal must contain at least one food")
    @Valid
    private List<MealFoodRequest> foods;
}
