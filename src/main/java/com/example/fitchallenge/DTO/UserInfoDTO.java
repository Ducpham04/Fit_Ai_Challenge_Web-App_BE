package com.example.fitchallenge.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho User Info API
 * GET /api/user/info và PUT /api/user/info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private Long infoId;
    private Long userId;
    private String userName;
    private String email;
    private String avatar; // linkImage
    
    // Body information
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private Integer age;
    private String gender;
    private String activityLevel; // sedentary, lightly active, moderately active, very active, extra active
    private BigDecimal bodyFatPct;
    
    // Calculated fields
    private BigDecimal bmi;
    private BigDecimal bmr;
    private BigDecimal recommendedCalories;
    
    // Goal
    private Long goalId;
    private String goalName;
    
    private String createdAt;
}



