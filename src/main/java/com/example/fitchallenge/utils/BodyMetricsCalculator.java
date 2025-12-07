package com.example.fitchallenge.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class để tính toán các chỉ số cơ thể
 * - BMI (Body Mass Index)
 * - BMR (Basal Metabolic Rate) - Mifflin-St Jeor Equation
 * - Recommended Calories
 */
public class BodyMetricsCalculator {

    /**
     * Tính BMI (Body Mass Index)
     * BMI = weight (kg) / (height (m))^2
     */
    public static BigDecimal calculateBMI(BigDecimal weightKg, BigDecimal heightCm) {
        if (weightKg == null || heightCm == null || 
            weightKg.compareTo(BigDecimal.ZERO) <= 0 || 
            heightCm.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // Chuyển height từ cm sang m
        BigDecimal heightM = heightCm.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        // BMI = weight / (height^2)
        BigDecimal heightSquared = heightM.multiply(heightM);
        BigDecimal bmi = weightKg.divide(heightSquared, 2, RoundingMode.HALF_UP);
        
        return bmi;
    }

    /**
     * Tính BMR (Basal Metabolic Rate) sử dụng Mifflin-St Jeor Equation
     * 
     * Nam: BMR = 10 × weight(kg) + 6.25 × height(cm) - 5 × age(years) + 5
     * Nữ: BMR = 10 × weight(kg) + 6.25 × height(cm) - 5 × age(years) - 161
     */
    public static BigDecimal calculateBMR(BigDecimal weightKg, BigDecimal heightCm, Integer age, String gender) {
        if (weightKg == null || heightCm == null || age == null || gender == null ||
            weightKg.compareTo(BigDecimal.ZERO) <= 0 || 
            heightCm.compareTo(BigDecimal.ZERO) <= 0 || 
            age <= 0) {
            return BigDecimal.ZERO;
        }

        // Tính phần chung
        BigDecimal baseBMR = weightKg.multiply(new BigDecimal("10"))
                .add(heightCm.multiply(new BigDecimal("6.25")))
                .subtract(new BigDecimal(age).multiply(new BigDecimal("5")));

        // Điều chỉnh theo giới tính
        String genderLower = gender.toLowerCase().trim();
        if (genderLower.equals("male") || genderLower.equals("nam") || genderLower.equals("m")) {
            baseBMR = baseBMR.add(new BigDecimal("5"));
        } else if (genderLower.equals("female") || genderLower.equals("nữ") || genderLower.equals("f")) {
            baseBMR = baseBMR.subtract(new BigDecimal("161"));
        }

        return baseBMR.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Tính lượng calories khuyến nghị dựa trên BMR và activity level
     * 
     * Activity levels:
     * - Sedentary (ít vận động): BMR × 1.2
     * - Lightly active (vận động nhẹ): BMR × 1.375
     * - Moderately active (vận động vừa): BMR × 1.55
     * - Very active (vận động nhiều): BMR × 1.725
     * - Extra active (vận động rất nhiều): BMR × 1.9
     */
    public static BigDecimal calculateRecommendedCalories(BigDecimal bmr, String activityLevel) {
        if (bmr == null || bmr.compareTo(BigDecimal.ZERO) <= 0 || activityLevel == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal multiplier;
        String levelLower = activityLevel.toLowerCase().trim();

        switch (levelLower) {
            case "sedentary":
            case "ít vận động":
            case "1":
                multiplier = new BigDecimal("1.2");
                break;
            case "lightly active":
            case "vận động nhẹ":
            case "2":
                multiplier = new BigDecimal("1.375");
                break;
            case "moderately active":
            case "vận động vừa":
            case "3":
                multiplier = new BigDecimal("1.55");
                break;
            case "very active":
            case "vận động nhiều":
            case "4":
                multiplier = new BigDecimal("1.725");
                break;
            case "extra active":
            case "vận động rất nhiều":
            case "5":
                multiplier = new BigDecimal("1.9");
                break;
            default:
                multiplier = new BigDecimal("1.2"); // Mặc định sedentary
        }

        return bmr.multiply(multiplier).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Tính tất cả các chỉ số cùng lúc
     */
    public static BodyMetricsResult calculateAll(
            BigDecimal weightKg, 
            BigDecimal heightCm, 
            Integer age, 
            String gender, 
            String activityLevel) {
        
        BigDecimal bmi = calculateBMI(weightKg, heightCm);
        BigDecimal bmr = calculateBMR(weightKg, heightCm, age, gender);
        BigDecimal recommendedCalories = calculateRecommendedCalories(bmr, activityLevel);

        return new BodyMetricsResult(bmi, bmr, recommendedCalories);
    }

    /**
     * Class để trả về kết quả tính toán
     */
    public static class BodyMetricsResult {
        private final BigDecimal bmi;
        private final BigDecimal bmr;
        private final BigDecimal recommendedCalories;

        public BodyMetricsResult(BigDecimal bmi, BigDecimal bmr, BigDecimal recommendedCalories) {
            this.bmi = bmi;
            this.bmr = bmr;
            this.recommendedCalories = recommendedCalories;
        }

        public BigDecimal getBmi() {
            return bmi;
        }

        public BigDecimal getBmr() {
            return bmr;
        }

        public BigDecimal getRecommendedCalories() {
            return recommendedCalories;
        }
    }
}


