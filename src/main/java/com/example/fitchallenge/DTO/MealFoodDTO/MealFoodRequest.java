package com.example.fitchallenge.DTO.MealFoodDTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFoodRequest {
    @NotNull(message = "Meal ID is required")
    private Long mealId;

    @NotNull(message = "Food ID is required")
    private Long foodId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1 gram")
    @Max(value = 5000, message = "Quantity must not exceed 5000 grams")
    private Integer quantityG;

}
