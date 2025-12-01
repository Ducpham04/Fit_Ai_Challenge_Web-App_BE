package com.example.FIT_Challenge.DTO.UserNutritionDTO;

import com.example.FIT_Challenge.DTO.MealDTO.MealResponse;
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
    private String userName;
    private Long nutritionPlanId;
    private String planTitle;
    private LocalDate startDate;
    private LocalDate endDate;

    private Map<Integer, List<MealResponse>> days;


    private String status;
}

