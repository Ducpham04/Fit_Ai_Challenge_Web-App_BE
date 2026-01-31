package com.example.fitchallenge.DTO.HealthProfileDTO;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Height is required")
    @DecimalMin(value = "50.0", message = "Height must be at least 50 cm")
    @DecimalMax(value = "300.0", message = "Height must not exceed 300 cm")
    private BigDecimal heightCm;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "20.0", message = "Weight must be at least 20 kg")
    @DecimalMax(value = "500.0", message = "Weight must not exceed 500 kg")
    private BigDecimal weightKg;

    @NotNull(message = "Age is required")
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 120, message = "Age must not exceed 120")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender; // MALE, FEMALE, OTHER

    // Đo vòng (để tính Navy Body Fat)

    @DecimalMin(value = "0.0", message = "Waist circumference must be >= 0")
    private BigDecimal waistCm; // Vòng eo

    @DecimalMin(value = "0.0", message = "Hip circumference must be >= 0")
    private BigDecimal hipCm; // Vòng hông (cần cho nữ)

    @DecimalMin(value = "0.0", message = "Neck circumference must be >= 0")
    private BigDecimal neckCm; // Vòng cổ

    @Size(max = 500, message = "Body image URL must not exceed 500 characters")
    private String bodyImageUrl; // Ảnh toàn thân (tùy chọn)

    // ========== NHÓM B: Thói quen sinh hoạt ==========

    @NotBlank(message = "Daily activity level is required")
    private String dailyActivityLevel; // sedentary, lightly_active, moderately_active, very_active, extra_active

    @Min(value = 0, message = "Workout frequency must be >= 0")
    @Max(value = 14, message = "Workout frequency must not exceed 14 sessions per week")
    private Integer workoutFrequencyPerWeek; // Số buổi tập/tuần

    @Size(max = 100, message = "Favorite exercise type must not exceed 100 characters")
    private String favoriteExerciseType; // Cardio, Yoga, Strength, HIIT, Mixed

    @Size(max = 100, message = "Current diet type must not exceed 100 characters")
    private String currentDietType; // high_carb, high_fat, high_protein, balanced, keto, vegan

    @Min(value = 0, message = "Sleep hours must be >= 0")
    @Max(value = 24, message = "Sleep hours must not exceed 24")
    private Integer sleepHoursPerDay; // Số giờ ngủ/ngày

    private String stressLevel; // low, medium, high

    @Size(max = 100, message = "Occupation must not exceed 100 characters")
    private String occupation; // office_worker, driver, construction, freelancer, student

    // ========== NHÓM C: Mục tiêu cá nhân ==========

    @NotBlank(message = "Primary goal is required")
    private String primaryGoal; // lose_weight, build_muscle, lose_fat, maintain_health, prepare_event

    @DecimalMin(value = "0.0", message = "Goal weight must be >= 0")
    private BigDecimal goalWeightKg;

    @DecimalMin(value = "0.0", message = "Goal body fat percentage must be >= 0")
    @DecimalMax(value = "100.0", message = "Goal body fat percentage must not exceed 100")
    private BigDecimal goalBodyFatPercent;

    @Min(value = 1, message = "Goal timeline must be at least 1 day")
    private Integer goalTimelineDays; // Thời gian đạt mục tiêu (ngày)

    @Size(max = 1000, message = "Goal description must not exceed 1000 characters")
    private String goalDescription;

    // ========== NHÓM D: Tình trạng sức khỏe ==========

    @Size(max = 1000, message = "Medical history must not exceed 1000 characters")
    private String medicalHistory; // Tiền sử bệnh lý

    @Size(max = 1000, message = "Current injuries must not exceed 1000 characters")
    private String currentInjuries; // Chấn thương hiện tại

    private String mobilityLevel; // excellent, good, limited, restricted

    private String availableEquipment; // none, dumbbells, resistance_bands, full_gym, bodyweight_only

    // ========== NHÓM E: Hành vi ăn uống ==========

    @Min(value = 1, message = "Meals per day must be at least 1")
    @Max(value = 10, message = "Meals per day must not exceed 10")
    private Integer mealsPerDay; // Số bữa ăn/ngày

    @Size(max = 1000, message = "Frequent foods must not exceed 1000 characters")
    private String frequentFoods; // Món ăn thường xuyên

    @DecimalMin(value = "0.0", message = "Water intake must be >= 0")
    @DecimalMax(value = "20.0", message = "Water intake must not exceed 20 liters per day")
    private BigDecimal waterIntakeLitersPerDay; // Lượng nước uống (L/ngày)

    private String alcoholConsumption; // none, occasional, regular, heavy

    private String smokingStatus; // none, occasional, regular
}




