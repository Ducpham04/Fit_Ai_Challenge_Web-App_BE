package com.example.fitchallenge.utils;

import java.math.BigDecimal;

/**
 * Utility class để tính toán calories đốt cháy dựa trên bài tập
 * Sử dụng MET (Metabolic Equivalent of Task) values
 */
public class CaloriesCalculator {

    /**
     * Tính calories đốt cháy dựa trên:
     * - Loại bài tập (exercise type)
     * - Thời gian tập (minutes)
     * - Số reps và sets
     * - Trọng lượng cơ thể (weight in kg) - optional
     * 
     * Formula: Calories = MET × weight(kg) × time(hours)
     * Hoặc: Calories = MET × 3.5 × weight(kg) × time(minutes) / 200
     * 
     * Nếu không có weight, sử dụng average weight (70kg)
     */
    public static Integer calculateCalories(
            String exerciseType,
            Integer durationMinutes,
            Integer sets,
            Integer reps,
            BigDecimal weightKg
    ) {
        if (durationMinutes == null || durationMinutes <= 0) {
            return 0;
        }

        // Default weight nếu không có
        double weight = weightKg != null && weightKg.compareTo(BigDecimal.ZERO) > 0
                ? weightKg.doubleValue()
                : 70.0; // Average weight

        // Get MET value based on exercise type
        double met = getMETValue(exerciseType, sets, reps);

        // Calculate calories: MET × weight(kg) × time(hours)
        // Hoặc: MET × 3.5 × weight(kg) × time(minutes) / 200
        double calories = met * 3.5 * weight * durationMinutes / 200.0;

        // Adjust based on sets and reps (more intensity = more calories)
        if (sets != null && sets > 0 && reps != null && reps > 0) {
            // Intensity multiplier: more sets/reps = higher intensity
            double intensityMultiplier = 1.0 + (sets * reps * 0.01); // 1% per set×rep
            calories *= intensityMultiplier;
        }

        return (int) Math.round(calories);
    }

    /**
     * Tính calories đơn giản hơn chỉ dựa trên exercise type và duration
     */
    public static Integer calculateCaloriesSimple(String exerciseType, Integer durationMinutes, BigDecimal weightKg) {
        return calculateCalories(exerciseType, durationMinutes, null, null, weightKg);
    }

    /**
     * Get MET (Metabolic Equivalent of Task) value based on exercise type
     * MET values from Compendium of Physical Activities
     */
    private static double getMETValue(String exerciseType, Integer sets, Integer reps) {
        if (exerciseType == null) {
            return 3.0; // Default moderate activity
        }

        String type = exerciseType.toLowerCase().trim();

        // Strength training exercises
        if (type.contains("push-up") || type.contains("pushup")) {
            return 8.0; // Push-ups: 8 METs
        }
        if (type.contains("pull-up") || type.contains("pullup")) {
            return 8.0; // Pull-ups: 8 METs
        }
        if (type.contains("squat")) {
            return 5.5; // Squats: 5.5 METs
        }
        if (type.contains("plank")) {
            return 3.0; // Plank: 3 METs
        }
        if (type.contains("burpee")) {
            return 10.0; // Burpees: 10 METs
        }
        if (type.contains("jump") || type.contains("jumping")) {
            return 8.0; // Jumping jacks: 8 METs
        }
        if (type.contains("lunge")) {
            return 5.0; // Lunges: 5 METs
        }
        if (type.contains("crunch") || type.contains("sit-up")) {
            return 3.0; // Crunches: 3 METs
        }
        if (type.contains("bicep") || type.contains("curl")) {
            return 3.0; // Bicep curls: 3 METs
        }
        if (type.contains("tricep")) {
            return 3.0; // Tricep exercises: 3 METs
        }
        if (type.contains("shoulder")) {
            return 3.0; // Shoulder exercises: 3 METs
        }

        // Cardio exercises
        if (type.contains("running") || type.contains("run")) {
            return 8.0; // Running: 8 METs
        }
        if (type.contains("walking") || type.contains("walk")) {
            return 3.5; // Walking: 3.5 METs
        }
        if (type.contains("cycling") || type.contains("bike")) {
            return 6.0; // Cycling: 6 METs
        }
        if (type.contains("swimming")) {
            return 7.0; // Swimming: 7 METs
        }

        // Yoga and flexibility
        if (type.contains("yoga")) {
            return 2.5; // Yoga: 2.5 METs
        }
        if (type.contains("stretch") || type.contains("flexibility")) {
            return 2.0; // Stretching: 2 METs
        }

        // HIIT and circuit training
        if (type.contains("hiit") || type.contains("circuit")) {
            return 8.0; // HIIT: 8 METs
        }
        if (type.contains("full-body") || type.contains("fullbody")) {
            return 6.0; // Full body workout: 6 METs
        }

        // Default: moderate intensity strength training
        return 5.0;
    }

    /**
     * Tính calories từ DailyTrainingLog với personalized plan data
     */
    public static Integer calculateCaloriesFromLog(
            Integer actualDurationMinutes,
            Integer setsCompleted,
            Integer repsCompleted,
            String exerciseType,
            BigDecimal userWeightKg
    ) {
        if (actualDurationMinutes == null || actualDurationMinutes <= 0) {
            // Nếu không có duration, estimate từ sets và reps
            if (setsCompleted != null && setsCompleted > 0 && repsCompleted != null && repsCompleted > 0) {
                // Estimate: mỗi set mất khoảng 2-3 phút (bao gồm rest)
                int estimatedMinutes = setsCompleted * 3;
                return calculateCalories(exerciseType, estimatedMinutes, setsCompleted, repsCompleted, userWeightKg);
            }
            return 0;
        }

        return calculateCalories(exerciseType, actualDurationMinutes, setsCompleted, repsCompleted, userWeightKg);
    }
}

