package com.example.FIT_Challenge.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long foodId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "calories_per_100g")
    private Integer caloriesPer100g;

    // ✅ BỎ precision và scale cho Double
    @Column(name = "protein_per_100g")
    private Double proteinPer100g;

    @Column(name = "carbs_per_100g")
    private Double carbsPer100g;

    @Column(name = "fat_per_100g")
    private Double fatPer100g;


    private String notes;
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealFood> mealFoods;

}