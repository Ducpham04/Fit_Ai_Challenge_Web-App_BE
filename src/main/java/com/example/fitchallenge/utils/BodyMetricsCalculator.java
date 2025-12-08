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
     * Tính TDEE (Total Daily Energy Expenditure) - tương đương với Recommended Calories
     * TDEE = BMR × Activity Multiplier
     */
    public static BigDecimal calculateTDEE(BigDecimal bmr, String activityLevel) {
        return calculateRecommendedCalories(bmr, activityLevel);
    }

    /**
     * Tính Body Fat % sử dụng Navy Body Fat Formula (rất chính xác)
     * 
     * Nam: BF% = 495 / (1.0324 - 0.19077 * log10(waist - neck) + 0.15456 * log10(height)) - 450
     * Nữ: BF% = 495 / (1.29579 - 0.35004 * log10(waist + hip - neck) + 0.22100 * log10(height)) - 450
     * 
     * @param heightCm Chiều cao (cm)
     * @param waistCm Vòng eo (cm)
     * @param neckCm Vòng cổ (cm)
     * @param hipCm Vòng hông (cm) - chỉ cần cho nữ, có thể null cho nam
     * @param gender Giới tính (MALE/FEMALE)
     * @return Body Fat % (0-100)
     */
    public static BigDecimal calculateNavyBodyFat(
            BigDecimal heightCm, 
            BigDecimal waistCm, 
            BigDecimal neckCm, 
            BigDecimal hipCm, 
            String gender) {
        
        if (heightCm == null || waistCm == null || neckCm == null ||
            heightCm.compareTo(BigDecimal.ZERO) <= 0 ||
            waistCm.compareTo(BigDecimal.ZERO) <= 0 ||
            neckCm.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        String genderLower = gender != null ? gender.toLowerCase().trim() : "";
        boolean isMale = genderLower.equals("male") || genderLower.equals("nam") || genderLower.equals("m");

        // Chuyển sang inches (Navy formula dùng inches)
        BigDecimal heightIn = heightCm.divide(new BigDecimal("2.54"), 2, RoundingMode.HALF_UP);
        BigDecimal waistIn = waistCm.divide(new BigDecimal("2.54"), 2, RoundingMode.HALF_UP);
        BigDecimal neckIn = neckCm.divide(new BigDecimal("2.54"), 2, RoundingMode.HALF_UP);

        BigDecimal bodyFat;
        
        if (isMale) {
            // Nam: BF% = 495 / (1.0324 - 0.19077 * log10(waist - neck) + 0.15456 * log10(height)) - 450
            BigDecimal waistNeckDiff = waistIn.subtract(neckIn);
            if (waistNeckDiff.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.ZERO;
            }
            
            double logWaistNeck = Math.log10(waistNeckDiff.doubleValue());
            double logHeight = Math.log10(heightIn.doubleValue());
            
            double denominator = 1.0324 - 0.19077 * logWaistNeck + 0.15456 * logHeight;
            if (denominator <= 0) {
                return BigDecimal.ZERO;
            }
            
            bodyFat = new BigDecimal(495.0 / denominator - 450.0);
        } else {
            // Nữ: BF% = 495 / (1.29579 - 0.35004 * log10(waist + hip - neck) + 0.22100 * log10(height)) - 450
            if (hipCm == null || hipCm.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.ZERO; // Cần hip cho nữ
            }
            
            BigDecimal hipIn = hipCm.divide(new BigDecimal("2.54"), 2, RoundingMode.HALF_UP);
            BigDecimal waistHipNeck = waistIn.add(hipIn).subtract(neckIn);
            if (waistHipNeck.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.ZERO;
            }
            
            double logWaistHipNeck = Math.log10(waistHipNeck.doubleValue());
            double logHeight = Math.log10(heightIn.doubleValue());
            
            double denominator = 1.29579 - 0.35004 * logWaistHipNeck + 0.22100 * logHeight;
            if (denominator <= 0) {
                return BigDecimal.ZERO;
            }
            
            bodyFat = new BigDecimal(495.0 / denominator - 450.0);
        }

        // Giới hạn body fat trong khoảng 0-100%
        if (bodyFat.compareTo(BigDecimal.ZERO) < 0) {
            bodyFat = BigDecimal.ZERO;
        } else if (bodyFat.compareTo(new BigDecimal("100")) > 0) {
            bodyFat = new BigDecimal("100");
        }

        return bodyFat.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Tính Lean Body Mass (LBM) - Khối lượng cơ nạc
     * LBM = Weight × (1 - BodyFat% / 100)
     * 
     * @param weightKg Cân nặng (kg)
     * @param bodyFatPercent Body Fat % (0-100)
     * @return Lean Body Mass (kg)
     */
    public static BigDecimal calculateLeanBodyMass(BigDecimal weightKg, BigDecimal bodyFatPercent) {
        if (weightKg == null || bodyFatPercent == null ||
            weightKg.compareTo(BigDecimal.ZERO) <= 0 ||
            bodyFatPercent.compareTo(BigDecimal.ZERO) < 0 ||
            bodyFatPercent.compareTo(new BigDecimal("100")) > 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal fatRatio = bodyFatPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal leanRatio = BigDecimal.ONE.subtract(fatRatio);
        
        return weightKg.multiply(leanRatio).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Tính Body Fat % từ BMI (ước tính, ít chính xác hơn Navy method)
     * 
     * Nam: BF% = (1.20 × BMI) + (0.23 × Age) - 16.2
     * Nữ: BF% = (1.20 × BMI) + (0.23 × Age) - 5.4
     */
    public static BigDecimal estimateBodyFatFromBMI(BigDecimal bmi, Integer age, String gender) {
        if (bmi == null || age == null || gender == null ||
            bmi.compareTo(BigDecimal.ZERO) <= 0 || age <= 0) {
            return BigDecimal.ZERO;
        }

        String genderLower = gender.toLowerCase().trim();
        boolean isMale = genderLower.equals("male") || genderLower.equals("nam") || genderLower.equals("m");

        BigDecimal baseBF = bmi.multiply(new BigDecimal("1.20"))
                .add(new BigDecimal(age).multiply(new BigDecimal("0.23")));

        if (isMale) {
            baseBF = baseBF.subtract(new BigDecimal("16.2"));
        } else {
            baseBF = baseBF.subtract(new BigDecimal("5.4"));
        }

        // Giới hạn trong khoảng 0-100%
        if (baseBF.compareTo(BigDecimal.ZERO) < 0) {
            baseBF = BigDecimal.ZERO;
        } else if (baseBF.compareTo(new BigDecimal("100")) > 0) {
            baseBF = new BigDecimal("100");
        }

        return baseBF.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Class để trả về kết quả tính toán
     */
    public static class BodyMetricsResult {
        private final BigDecimal bmi;
        private final BigDecimal bmr;
        private final BigDecimal recommendedCalories;
        private final BigDecimal tdee;
        private final BigDecimal bodyFat;
        private final BigDecimal leanBodyMass;

        public BodyMetricsResult(BigDecimal bmi, BigDecimal bmr, BigDecimal recommendedCalories) {
            this.bmi = bmi;
            this.bmr = bmr;
            this.recommendedCalories = recommendedCalories;
            this.tdee = recommendedCalories; // TDEE = Recommended Calories
            this.bodyFat = null;
            this.leanBodyMass = null;
        }

        public BodyMetricsResult(BigDecimal bmi, BigDecimal bmr, BigDecimal recommendedCalories, 
                                BigDecimal bodyFat, BigDecimal leanBodyMass) {
            this.bmi = bmi;
            this.bmr = bmr;
            this.recommendedCalories = recommendedCalories;
            this.tdee = recommendedCalories;
            this.bodyFat = bodyFat;
            this.leanBodyMass = leanBodyMass;
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

        public BigDecimal getTdee() {
            return tdee;
        }

        public BigDecimal getBodyFat() {
            return bodyFat;
        }

        public BigDecimal getLeanBodyMass() {
            return leanBodyMass;
        }
    }
}


