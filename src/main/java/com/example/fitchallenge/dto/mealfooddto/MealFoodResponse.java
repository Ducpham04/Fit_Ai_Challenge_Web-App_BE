package com.example.fitchallenge.dto.mealfooddto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFoodResponse {
    private Long mfId;

    private Long foodId;
    private String foodName;
    private Integer quantityG;

    // Dinh duõng
    private Integer totalCalories ;
    private Double totalProtein ;
    private Double totalCarbs ;
    private Double totalFat ;
}

