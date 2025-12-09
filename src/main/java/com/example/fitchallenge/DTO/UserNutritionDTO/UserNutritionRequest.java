package com.example.fitchallenge.DTO.UserNutritionDTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNutritionRequest {
    private Long userId;
    private Long nutritionPlanId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // active / completed / canceled / paused
}
