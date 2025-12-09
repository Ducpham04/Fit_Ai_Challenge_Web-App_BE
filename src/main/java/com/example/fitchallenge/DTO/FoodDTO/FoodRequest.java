package com.example.fitchallenge.DTO.FoodDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodRequest {
    private String name;
    private Integer calories;
    private Double protein;
    private Double carbs ;
    private Double fat;
    private String notes;
}
