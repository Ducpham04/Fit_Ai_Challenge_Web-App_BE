package com.example.fitchallenge.DTO.HealthProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho Health Profile Request
 * Bao gồm tất cả thông tin cần thiết để tạo/cập nhật Health Profile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthProfileRequest {
    
    // ========== NHÓM A: Thông tin cơ thể (bắt buộc) ==========
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private Integer age;
    private String gender; // MALE, FEMALE, OTHER
    
    // Đo vòng (để tính Navy Body Fat)
    private BigDecimal waistCm; // Vòng eo
    private BigDecimal hipCm; // Vòng hông (cần cho nữ)
    private BigDecimal neckCm; // Vòng cổ
    
    private String bodyImageUrl; // Ảnh toàn thân (tùy chọn)
    
    // ========== NHÓM B: Thói quen sinh hoạt ==========
    private String dailyActivityLevel; // sedentary, lightly_active, moderately_active, very_active, extra_active
    private Integer workoutFrequencyPerWeek; // Số buổi tập/tuần
    private String favoriteExerciseType; // Cardio, Yoga, Strength, HIIT, Mixed
    private String currentDietType; // high_carb, high_fat, high_protein, balanced, keto, vegan
    private Integer sleepHoursPerDay; // Số giờ ngủ/ngày
    private String stressLevel; // low, medium, high
    private String occupation; // office_worker, driver, construction, freelancer, student
    
    // ========== NHÓM C: Mục tiêu cá nhân ==========
    private String primaryGoal; // lose_weight, build_muscle, lose_fat, maintain_health, prepare_event
    private BigDecimal goalWeightKg;
    private BigDecimal goalBodyFatPercent;
    private Integer goalTimelineDays; // Thời gian đạt mục tiêu (ngày)
    private String goalDescription;
    
    // ========== NHÓM D: Tình trạng sức khỏe ==========
    private String medicalHistory; // Tiền sử bệnh lý
    private String currentInjuries; // Chấn thương hiện tại
    private String mobilityLevel; // excellent, good, limited, restricted
    private String availableEquipment; // none, dumbbells, resistance_bands, full_gym, bodyweight_only
    
    // ========== NHÓM E: Hành vi ăn uống ==========
    private Integer mealsPerDay; // Số bữa ăn/ngày
    private String frequentFoods; // Món ăn thường xuyên
    private BigDecimal waterIntakeLitersPerDay; // Lượng nước uống (L/ngày)
    private String alcoholConsumption; // none, occasional, regular, heavy
    private String smokingStatus; // none, occasional, regular
}


