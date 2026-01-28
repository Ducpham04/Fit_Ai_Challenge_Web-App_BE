package com.example.fitchallenge.dto.healthprofiledto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO cho Health Profile Response
 * Bao gồm tất cả thông tin + kết quả tính toán
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthProfileResponse {
    
    private Long id;
    private Long userId;
    
    // ========== NHÓM A: Thông tin cơ thể ==========
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private Integer age;
    private String gender;
    private BigDecimal waistCm;
    private BigDecimal hipCm;
    private BigDecimal neckCm;
    private String bodyImageUrl;
    
    // ========== Tính toán tự động ==========
    private BigDecimal bmi;
    private BigDecimal bmr;
    private BigDecimal tdee;
    private BigDecimal bodyFatPercent;
    private BigDecimal leanBodyMassKg;
    private BigDecimal recommendedCalories;
    
    // ========== NHÓM B: Thói quen sinh hoạt ==========
    private String dailyActivityLevel;
    private Integer workoutFrequencyPerWeek;
    private String favoriteExerciseType;
    private String currentDietType;
    private Integer sleepHoursPerDay;
    private String stressLevel;
    private String occupation;
    
    // ========== NHÓM C: Mục tiêu cá nhân ==========
    private String primaryGoal;
    private BigDecimal goalWeightKg;
    private BigDecimal goalBodyFatPercent;
    private Integer goalTimelineDays;
    private String goalDescription;
    
    // ========== NHÓM D: Tình trạng sức khỏe ==========
    private String medicalHistory;
    private String currentInjuries;
    private String mobilityLevel;
    private String availableEquipment;
    
    // ========== NHÓM E: Hành vi ăn uống ==========
    private Integer mealsPerDay;
    private String frequentFoods;
    private BigDecimal waterIntakeLitersPerDay;
    private String alcoholConsumption;
    private String smokingStatus;
    
    // ========== Timestamps ==========
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}




