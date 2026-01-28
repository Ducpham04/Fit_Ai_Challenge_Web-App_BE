package com.example.fitchallenge.dto.usernutritionfto;

import com.example.fitchallenge.dto.mealdto.MealResponse;
import lombok.*;

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

