package com.example.FIT_Challenge.DTO.MealFoodDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFoodResponse {
    private Long mfId;
    private Long mealId;
    private Long foodId;
    private Integer quantityG;
}

