package com.example.fitchallenge.DTO.UserNutritionDTO;

import com.example.fitchallenge.DTO.MealDTO.MealResponse;
import lombok.*;

import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNutritionResponse {
    private Long unId;
    private Long userId;

    private Long nutritionPlanId;
    private String planTitle;
    private String  targetCalories ;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double completion ;
    private Map<Integer, List<MealResponse>> days;
    private String status;
}

