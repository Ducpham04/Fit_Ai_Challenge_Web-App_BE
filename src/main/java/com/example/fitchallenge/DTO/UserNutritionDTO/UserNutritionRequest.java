package com.example.fitchallenge.DTO.UserNutritionDTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNutritionRequest {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Nutrition plan ID is required")
    @Min(value = 1, message = "Nutrition plan ID must be a positive number")
    private Long nutritionPlanId;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "^(active|completed|canceled|paused)$",
            message = "Status must be one of: active, completed, canceled, paused"
    )
    private String status; // active / completed / canceled / paused
}
