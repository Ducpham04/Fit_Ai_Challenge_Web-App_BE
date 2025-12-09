package com.example.fitchallenge.DTO.MealFoodDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFoodRequest {
    private Long mealId;
    private Long foodId;
    private Integer quantityG;

}
