package com.example.fitchallenge.DTO.FoodDTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodRequest {
    @NotBlank(message = "Food name must not be blank")
    @Size(max = 255, message = "Food name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Calories must not be null")
    @Min(value = 0, message = "Calories must be greater than or equal to 0")
    private Integer calories;

    @NotNull(message = "Protein must not be null")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "Protein must be greater than or equal to 0")
    private Double protein;

    @NotNull(message = "Carbohydrates must not be null")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "Carbohydrates must be greater than or equal to 0")
    private Double carbs;

    @NotNull(message = "Fat must not be null")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "Fat must be greater than or equal to 0")
    private Double fat;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
