package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.HealthProfileDTO.HealthProfileRequest;
import com.example.fitchallenge.DTO.HealthProfileDTO.HealthProfileResponse;
import com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO;
import com.example.fitchallenge.Entity.HealthProfile;
import com.example.fitchallenge.Entity.TrainingPlan;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.HealthProfileRepository;
import com.example.fitchallenge.repository.TrainingPlanRepository;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.service.HealthProfileService;
import com.example.fitchallenge.utils.BodyMetricsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthProfileServiceImpl implements HealthProfileService {

    private final HealthProfileRepository healthProfileRepository;
    private final UserRepository userRepository;
    private final TrainingPlanRepository trainingPlanRepository;

    @Override
    @Transactional
    public NotificationResponse createOrUpdateHealthProfile(Long userId, HealthProfileRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Optional<HealthProfile> existingProfile = healthProfileRepository.findByUser_Id(userId);
            HealthProfile profile;

            if (existingProfile.isPresent()) {
                profile = existingProfile.get();
            } else {
                profile = new HealthProfile();
                profile.setUser(user);
            }

            // ========== Cập nhật thông tin cơ thể ==========
            profile.setHeightCm(request.getHeightCm());
            profile.setWeightKg(request.getWeightKg());
            profile.setAge(request.getAge());
            profile.setGender(request.getGender());
            profile.setWaistCm(request.getWaistCm());
            profile.setHipCm(request.getHipCm());
            profile.setNeckCm(request.getNeckCm());
            profile.setBodyImageUrl(request.getBodyImageUrl());

            // ========== Cập nhật thói quen sinh hoạt ==========
            profile.setDailyActivityLevel(request.getDailyActivityLevel());
            profile.setWorkoutFrequencyPerWeek(request.getWorkoutFrequencyPerWeek());
            profile.setFavoriteExerciseType(request.getFavoriteExerciseType());
            profile.setCurrentDietType(request.getCurrentDietType());
            profile.setSleepHoursPerDay(request.getSleepHoursPerDay());
            profile.setStressLevel(request.getStressLevel());
            profile.setOccupation(request.getOccupation());

            // ========== Cập nhật mục tiêu ==========
            profile.setPrimaryGoal(request.getPrimaryGoal());
            profile.setGoalWeightKg(request.getGoalWeightKg());
            profile.setGoalBodyFatPercent(request.getGoalBodyFatPercent());
            profile.setGoalTimelineDays(request.getGoalTimelineDays());
            profile.setGoalDescription(request.getGoalDescription());

            // ========== Cập nhật sức khỏe ==========
            profile.setMedicalHistory(request.getMedicalHistory());
            profile.setCurrentInjuries(request.getCurrentInjuries());
            profile.setMobilityLevel(request.getMobilityLevel());
            profile.setAvailableEquipment(request.getAvailableEquipment());

            // ========== Cập nhật hành vi ăn uống ==========
            profile.setMealsPerDay(request.getMealsPerDay());
            profile.setFrequentFoods(request.getFrequentFoods());
            profile.setWaterIntakeLitersPerDay(request.getWaterIntakeLitersPerDay());
            profile.setAlcoholConsumption(request.getAlcoholConsumption());
            profile.setSmokingStatus(request.getSmokingStatus());

            // ========== TỰ ĐỘNG TÍNH TOÁN ==========
            if (profile.getHeightCm() != null && profile.getWeightKg() != null) {
                // Tính BMI
                BigDecimal bmi = BodyMetricsCalculator.calculateBMI(
                        profile.getWeightKg(), 
                        profile.getHeightCm()
                );
                profile.setBmi(bmi);

                // Tính BMR
                if (profile.getAge() != null && profile.getGender() != null) {
                    BigDecimal bmr = BodyMetricsCalculator.calculateBMR(
                            profile.getWeightKg(),
                            profile.getHeightCm(),
                            profile.getAge(),
                            profile.getGender()
                    );
                    profile.setBmr(bmr);

                    // Tính TDEE
                    String activityLevel = profile.getDailyActivityLevel() != null 
                            ? profile.getDailyActivityLevel() 
                            : "sedentary";
                    BigDecimal tdee = BodyMetricsCalculator.calculateTDEE(bmr, activityLevel);
                    profile.setTdee(tdee);
                    profile.setRecommendedCalories(tdee);
                }

                // Tính Body Fat % - ưu tiên Navy method nếu có đủ số đo
                BigDecimal bodyFat = null;
                if (profile.getWaistCm() != null && profile.getNeckCm() != null) {
                    // Có đủ số đo để tính Navy Body Fat
                    bodyFat = BodyMetricsCalculator.calculateNavyBodyFat(
                            profile.getHeightCm(),
                            profile.getWaistCm(),
                            profile.getNeckCm(),
                            profile.getHipCm(), // Có thể null cho nam
                            profile.getGender()
                    );
                } else if (profile.getBmi() != null && profile.getAge() != null && profile.getGender() != null) {
                    // Ước tính từ BMI nếu không có số đo vòng
                    bodyFat = BodyMetricsCalculator.estimateBodyFatFromBMI(
                            profile.getBmi(),
                            profile.getAge(),
                            profile.getGender()
                    );
                }
                profile.setBodyFatPercent(bodyFat);

                // Tính Lean Body Mass
                if (bodyFat != null) {
                    BigDecimal leanBodyMass = BodyMetricsCalculator.calculateLeanBodyMass(
                            profile.getWeightKg(),
                            bodyFat
                    );
                    profile.setLeanBodyMassKg(leanBodyMass);
                }
            }

            HealthProfile saved = healthProfileRepository.save(profile);
            HealthProfileResponse response = toResponse(saved);

            return new NotificationResponse(true, "Health profile saved successfully", response);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, "Error saving health profile: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getHealthProfile(Long userId) {
        try {
            Optional<HealthProfile> profileOpt = healthProfileRepository.findByUser_Id(userId);
            if (profileOpt.isEmpty()) {
                return new NotificationResponse(false, "Health profile not found for user");
            }

            HealthProfileResponse response = toResponse(profileOpt.get());
            return new NotificationResponse(true, "Health profile retrieved successfully", response);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error retrieving health profile: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getRecommendedTrainingPlans(Long userId) {
        try {
            Optional<HealthProfile> profileOpt = healthProfileRepository.findByUser_Id(userId);
            if (profileOpt.isEmpty()) {
                return new NotificationResponse(false, "Health profile not found. Please complete your health profile first.");
            }

            HealthProfile profile = profileOpt.get();
            List<TrainingPlan> allPlans = trainingPlanRepository.findAll();

            // Tính điểm phù hợp cho mỗi plan
            List<PlanScore> planScores = allPlans.stream()
                    .map(plan -> calculatePlanScore(plan, profile))
                    .sorted((a, b) -> Double.compare(b.score, a.score)) // Sắp xếp theo điểm giảm dần
                    .limit(10) // Lấy top 10
                    .collect(Collectors.toList());

            // Chuyển sang DTO
            List<TrainingPlanResponseDTO> recommendedPlans = planScores.stream()
                    .map(ps -> mapToTrainingPlanDTO(ps.plan))
                    .collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("recommendedPlans", recommendedPlans);
            result.put("reasoning", generateRecommendationReasoning(profile, planScores));

            return new NotificationResponse(true, "Recommended training plans retrieved successfully", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, "Error getting recommended plans: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public NotificationResponse generatePersonalizedPlanDetail(Long userId, Long trainingPlanId) {
        try {
            Optional<HealthProfile> profileOpt = healthProfileRepository.findByUser_Id(userId);
            if (profileOpt.isEmpty()) {
                return new NotificationResponse(false, "Health profile not found. Please complete your health profile first.");
            }

            Optional<TrainingPlan> planOpt = trainingPlanRepository.findById(trainingPlanId);
            if (planOpt.isEmpty()) {
                return new NotificationResponse(false, "Training plan not found");
            }

            HealthProfile profile = profileOpt.get();
            TrainingPlan plan = planOpt.get();

            // Logic sinh PersonalPlanDetail sẽ được implement trong PersonalizationService
            // Tạm thời trả về thông báo với thông tin plan và profile
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Personalized plan detail generation will be implemented in PersonalizationService");
            result.put("trainingPlanId", trainingPlanId);
            result.put("trainingPlanTitle", plan.getTitle());
            result.put("userId", userId);
            result.put("bodyFatPercent", profile.getBodyFatPercent());
            result.put("primaryGoal", profile.getPrimaryGoal());

            return new NotificationResponse(true, "Personalized plan detail generation initiated", result);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error generating personalized plan: " + e.getMessage());
        }
    }

    // ========== Helper Methods ==========

    private HealthProfileResponse toResponse(HealthProfile profile) {
        HealthProfileResponse response = new HealthProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        
        // Thông tin cơ thể
        response.setHeightCm(profile.getHeightCm());
        response.setWeightKg(profile.getWeightKg());
        response.setAge(profile.getAge());
        response.setGender(profile.getGender());
        response.setWaistCm(profile.getWaistCm());
        response.setHipCm(profile.getHipCm());
        response.setNeckCm(profile.getNeckCm());
        response.setBodyImageUrl(profile.getBodyImageUrl());
        
        // Tính toán
        response.setBmi(profile.getBmi());
        response.setBmr(profile.getBmr());
        response.setTdee(profile.getTdee());
        response.setBodyFatPercent(profile.getBodyFatPercent());
        response.setLeanBodyMassKg(profile.getLeanBodyMassKg());
        response.setRecommendedCalories(profile.getRecommendedCalories());
        
        // Thói quen sinh hoạt
        response.setDailyActivityLevel(profile.getDailyActivityLevel());
        response.setWorkoutFrequencyPerWeek(profile.getWorkoutFrequencyPerWeek());
        response.setFavoriteExerciseType(profile.getFavoriteExerciseType());
        response.setCurrentDietType(profile.getCurrentDietType());
        response.setSleepHoursPerDay(profile.getSleepHoursPerDay());
        response.setStressLevel(profile.getStressLevel());
        response.setOccupation(profile.getOccupation());
        
        // Mục tiêu
        response.setPrimaryGoal(profile.getPrimaryGoal());
        response.setGoalWeightKg(profile.getGoalWeightKg());
        response.setGoalBodyFatPercent(profile.getGoalBodyFatPercent());
        response.setGoalTimelineDays(profile.getGoalTimelineDays());
        response.setGoalDescription(profile.getGoalDescription());
        
        // Sức khỏe
        response.setMedicalHistory(profile.getMedicalHistory());
        response.setCurrentInjuries(profile.getCurrentInjuries());
        response.setMobilityLevel(profile.getMobilityLevel());
        response.setAvailableEquipment(profile.getAvailableEquipment());
        
        // Hành vi ăn uống
        response.setMealsPerDay(profile.getMealsPerDay());
        response.setFrequentFoods(profile.getFrequentFoods());
        response.setWaterIntakeLitersPerDay(profile.getWaterIntakeLitersPerDay());
        response.setAlcoholConsumption(profile.getAlcoholConsumption());
        response.setSmokingStatus(profile.getSmokingStatus());
        
        // Timestamps
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());
        
        return response;
    }

    /**
     * Tính điểm phù hợp của TrainingPlan với HealthProfile
     */
    private PlanScore calculatePlanScore(TrainingPlan plan, HealthProfile profile) {
        double score = 0.0;
        List<String> reasons = new ArrayList<>();

        // 1. Kiểm tra Body Fat và mục tiêu (30 điểm)
        if (profile.getBodyFatPercent() != null && profile.getPrimaryGoal() != null) {
            double bodyFat = profile.getBodyFatPercent().doubleValue();
            String goal = profile.getPrimaryGoal().toLowerCase();
            String difficulty = plan.getDifficultyLevel() != null ? plan.getDifficultyLevel().toLowerCase() : "";

            if (goal.contains("lose") || goal.contains("fat") || goal.contains("weight")) {
                // Mục tiêu giảm mỡ/cân
                if (bodyFat > 25) {
                    // Body fat cao → ưu tiên Cardio/HIIT
                    if (difficulty.contains("beginner") || difficulty.contains("intermediate")) {
                        score += 25;
                        reasons.add("High body fat - beginner/intermediate plan recommended");
                    }
                } else if (bodyFat > 18) {
                    // Body fat trung bình
                    if (difficulty.contains("intermediate")) {
                        score += 20;
                        reasons.add("Moderate body fat - intermediate plan suitable");
                    }
                }
            } else if (goal.contains("build") || goal.contains("muscle")) {
                // Mục tiêu tăng cơ
                if (bodyFat < 20) {
                    // Body fat thấp → ưu tiên Strength
                    if (difficulty.contains("intermediate") || difficulty.contains("advanced")) {
                        score += 25;
                        reasons.add("Low body fat - strength training recommended");
                    }
                }
            } else if (goal.contains("maintain")) {
                // Duy trì
                score += 15;
                reasons.add("Maintenance goal - any plan suitable");
            }
        }

        // 2. Kiểm tra thiết bị sẵn có (20 điểm)
        if (profile.getAvailableEquipment() != null && plan.getDifficultyLevel() != null) {
            String equipment = profile.getAvailableEquipment().toLowerCase();
            String difficulty = plan.getDifficultyLevel().toLowerCase();

            if (equipment.contains("none") || equipment.contains("bodyweight")) {
                // Chỉ có bodyweight
                if (difficulty.contains("beginner")) {
                    score += 20;
                    reasons.add("Bodyweight-only equipment - beginner plan suitable");
                } else {
                    score -= 10; // Trừ điểm nếu plan cần thiết bị
                }
            } else if (equipment.contains("full_gym") || equipment.contains("dumbbells")) {
                // Có thiết bị đầy đủ
                score += 15;
                reasons.add("Full equipment available - any plan suitable");
            }
        }

        // 3. Kiểm tra mức độ vận động (15 điểm)
        if (profile.getDailyActivityLevel() != null && profile.getWorkoutFrequencyPerWeek() != null) {
            String activityLevel = profile.getDailyActivityLevel().toLowerCase();
            int frequency = profile.getWorkoutFrequencyPerWeek();

            if (activityLevel.contains("sedentary") || frequency < 3) {
                // Ít vận động
                if (plan.getDifficultyLevel() != null && plan.getDifficultyLevel().toLowerCase().contains("beginner")) {
                    score += 15;
                    reasons.add("Low activity level - beginner plan recommended");
                }
            } else if (frequency >= 5) {
                // Tập nhiều
                if (plan.getDifficultyLevel() != null && 
                    (plan.getDifficultyLevel().toLowerCase().contains("intermediate") || 
                     plan.getDifficultyLevel().toLowerCase().contains("advanced"))) {
                    score += 15;
                    reasons.add("High workout frequency - intermediate/advanced plan suitable");
                }
            }
        }

        // 4. Kiểm tra chấn thương (20 điểm) - trừ điểm nếu plan không phù hợp
        if (profile.getCurrentInjuries() != null && !profile.getCurrentInjuries().isEmpty()) {
            String injuries = profile.getCurrentInjuries().toLowerCase();
            String difficulty = plan.getDifficultyLevel() != null ? plan.getDifficultyLevel().toLowerCase() : "";

            if (injuries.contains("back") || injuries.contains("spine")) {
                // Đau lưng → tránh plan nặng
                if (difficulty.contains("advanced")) {
                    score -= 20;
                    reasons.add("Back injury - advanced plan not recommended");
                } else {
                    score += 10;
                    reasons.add("Back injury - lighter plan recommended");
                }
            }
            if (injuries.contains("knee") || injuries.contains("leg")) {
                // Đau gối → tránh plan nhiều squat/jump
                if (difficulty.contains("advanced")) {
                    score -= 15;
                } else {
                    score += 10;
                }
            }
        }

        // 5. Kiểm tra loại bài tập yêu thích (10 điểm)
        if (profile.getFavoriteExerciseType() != null) {
            String favorite = profile.getFavoriteExerciseType().toLowerCase();
            String planTitle = plan.getTitle() != null ? plan.getTitle().toLowerCase() : "";
            String planDesc = plan.getDescription() != null ? plan.getDescription().toLowerCase() : "";

            if (planTitle.contains(favorite) || planDesc.contains(favorite)) {
                score += 10;
                reasons.add("Matches favorite exercise type: " + favorite);
            }
        }

        // 6. Kiểm tra stress level (5 điểm)
        if (profile.getStressLevel() != null) {
            String stress = profile.getStressLevel().toLowerCase();
            if (stress.contains("high")) {
                // Stress cao → tránh plan quá nặng
                if (plan.getDifficultyLevel() != null && plan.getDifficultyLevel().toLowerCase().contains("beginner")) {
                    score += 5;
                    reasons.add("High stress - lighter plan recommended");
                } else if (plan.getDifficultyLevel() != null && plan.getDifficultyLevel().toLowerCase().contains("advanced")) {
                    score -= 5;
                }
            }
        }

        return new PlanScore(plan, score, reasons);
    }

    private TrainingPlanResponseDTO mapToTrainingPlanDTO(TrainingPlan plan) {
        TrainingPlanResponseDTO dto = TrainingPlanResponseDTO.builder()
                .id(plan.getTpId())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .difficulty(plan.getDifficultyLevel())
                .duration(plan.getDurationWeeks())
                .build();
        
        if (plan.getGoal() != null) {
            dto.setGoalId(plan.getGoal().getId());
            dto.setGoalName(plan.getGoal().getName());
        }
        
        return dto;
    }

    private Map<String, Object> generateRecommendationReasoning(HealthProfile profile, List<PlanScore> planScores) {
        Map<String, Object> reasoning = new HashMap<>();
        reasoning.put("bodyFatPercent", profile.getBodyFatPercent());
        reasoning.put("primaryGoal", profile.getPrimaryGoal());
        reasoning.put("topRecommendations", planScores.stream()
                .limit(3)
                .map(ps -> Map.of(
                        "planId", ps.plan.getTpId(),
                        "planTitle", ps.plan.getTitle(),
                        "score", ps.score,
                        "reasons", ps.reasons
                ))
                .collect(Collectors.toList()));
        return reasoning;
    }

    // Inner class để lưu điểm và lý do
    private static class PlanScore {
        TrainingPlan plan;
        double score;
        List<String> reasons;

        PlanScore(TrainingPlan plan, double score, List<String> reasons) {
            this.plan = plan;
            this.score = score;
            this.reasons = reasons;
        }
    }
}

