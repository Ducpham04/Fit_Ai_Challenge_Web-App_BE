package com.example.fitchallenge.DTO.NutritionPlanDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NutritionPlanRequest {
    @NotNull(message = "Goal ID is required")
    private Long goalId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Calories per day is required")
    @Min(value = 0, message = "Calories per day must be greater than or equal to 0")
    private Integer caloriesPerDay;

    @NotNull(message = "Protein amount is required")
    @Min(value = 0, message = "Protein must be greater than or equal to 0")
    private Integer proteinG;

    @NotNull(message = "Carbohydrates amount is required")
    @Min(value = 0, message = "Carbohydrates must be greater than or equal to 0")
    private Integer carbsG;

    @NotNull(message = "Fat amount is required")
    @Min(value = 0, message = "Fat must be greater than or equal to 0")
    private Integer fatG;

    @NotBlank(message = "Status is required")
    private String status; // DRAFT, ACTIVE, COMPLETED
}