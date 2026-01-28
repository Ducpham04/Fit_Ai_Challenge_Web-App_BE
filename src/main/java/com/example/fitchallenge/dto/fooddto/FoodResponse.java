package com.example.fitchallenge.dto.fooddto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponse {
    private Long id;
    private String name;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private String notes;
}
