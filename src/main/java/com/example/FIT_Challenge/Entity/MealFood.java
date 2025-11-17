package com.example.FIT_Challenge.Entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meal_foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mfId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(name = "quantity_g", nullable = false)
    private Integer quantityG;
}

