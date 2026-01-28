package com.example.fitchallenge.dto.mealfooddto;

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
