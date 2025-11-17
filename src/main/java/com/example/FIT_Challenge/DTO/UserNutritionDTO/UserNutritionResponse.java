package com.example.FIT_Challenge.DTO.UserNutritionDTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNutritionResponse {
    private Long unId;
    private Long userId;
    private String userName;
    private Long nutritionPlanId;
    private String planTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}

