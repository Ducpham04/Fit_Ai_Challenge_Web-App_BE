package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "information_body_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformationBodyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long infoId;

    // Quan hệ nhiều - 1 với bảng users
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // => Entity Users cần có user_id

    @Column(name = "height_cm", precision = 5, scale = 2, nullable = false)
    private BigDecimal heightCm;

    @Column(name = "weight_kg", precision = 6, scale = 2, nullable = false)
    private BigDecimal weightKg;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", length = 20, nullable = false)
    private String gender;

    @Column(name = "body_fat_pct", precision = 5, scale = 2)
    private BigDecimal bodyFatPct;

    @Column(name = "bmi", precision = 5, scale = 2)
    private BigDecimal bmi;

    @Column(name = "activity_level", length = 50)
    private String activityLevel; // sedentary, lightly active, moderately active, very active, extra active

    @Column(name = "bmr", precision = 8, scale = 2)
    private BigDecimal bmr; // Basal Metabolic Rate

    @Column(name = "recommended_calories", precision = 8, scale = 0)
    private BigDecimal recommendedCalories;

    // Quan hệ nhiều - 1 với bảng goals
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goals goals;  // => Entity Goals cần có goal_id

    @Column(name = "created_at")
    private ZonedDateTime createdAt;
}
