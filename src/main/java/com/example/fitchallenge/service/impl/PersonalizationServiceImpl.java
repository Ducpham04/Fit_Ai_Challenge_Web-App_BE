package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.entity.UserTraining;
import com.example.fitchallenge.entity.PersonalizedPlanDetail;
import com.example.fitchallenge.entity.TrainingPlanDetail;
import com.example.fitchallenge.entity.User;
import com.example.fitchallenge.entity.InformationBodyUser;
import com.example.fitchallenge.entity.HealthProfile;
import com.example.fitchallenge.entity.Challenges;
import com.example.fitchallenge.dto.personalizedplandetaildto.PersonalizedPlanDetailResponse;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.*;
import com.example.fitchallenge.service.PersonalizationService;
import com.example.fitchallenge.utils.CaloriesCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalizationServiceImpl implements PersonalizationService {

    private final PersonalizedPlanDetailRepository personalizedPlanDetailRepository;
    private final UserTrainingRepository userTrainingRepository;
    private final TrainingPlanDetailRepository trainingPlanDetailRepository;
    private final HealthProfileRepository healthProfileRepository;
    private final InformationBodyUserRepository informationBodyUserRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Tạo PersonalizedPlanDetail cho user khi bắt đầu training plan
     * Dựa trên Health Profile để chọn template phù hợp và cá nhân hóa
     * 
     * Sử dụng REQUIRES_NEW để tách transaction riêng, tránh rollback transaction cha
     * Không throw exception để tránh "Transaction silently rolled back" warning
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public NotificationResponse createPersonalizedPlanDetails(Long utId) {
        try {
            System.out.println("🔄 [PersonalizationService] Creating personalized plan details for utId: " + utId);
            
            UserTraining userTraining = userTrainingRepository.findById(utId)
                    .orElseThrow(() -> {
                        System.out.println("❌ [PersonalizationService] UserTraining not found: " + utId);
                        return new RuntimeException("UserTraining not found with id: " + utId);
                    });

            User user = userTraining.getUser();
            Long userId = user.getId();
            System.out.println("✅ [PersonalizationService] Found UserTraining, userId: " + userId);

            // Get Health Profile (ưu tiên hơn UserBodyProfile)
            Optional<HealthProfile> healthProfileOpt = healthProfileRepository.findByUser_Id(userId);

            if (healthProfileOpt.isEmpty()) {
                System.out.println("⚠️ [PersonalizationService] Health Profile not found for userId: " + userId);
                System.out.println("⚠️ [PersonalizationService] Will use default values instead of personalization");
                // Không throw exception, sẽ dùng default values
                // return new NotificationResponse(false, 
                //         "Health Profile not found. Please complete your health profile first.");
            }

            HealthProfile healthProfile = healthProfileOpt.orElse(null);
            if (healthProfile != null) {
                System.out.println("✅ [PersonalizationService] Found Health Profile for userId: " + userId);
            } else {
                System.out.println("⚠️ [PersonalizationService] No Health Profile, using default values");
            }

            // Get all training plan details from template
            Long trainingPlanId = userTraining.getTrainingPlan().getTpId();
            System.out.println("📋 [PersonalizationService] Loading template details for trainingPlanId: " + trainingPlanId);
            
            List<TrainingPlanDetail> templateDetails = trainingPlanDetailRepository
                    .findByTrainingPlan_TpId(trainingPlanId);

            if (templateDetails.isEmpty()) {
                System.out.println("⚠️ [PersonalizationService] No template details found for trainingPlanId: " + trainingPlanId);
                return new NotificationResponse(false, 
                        "Training plan has no exercises. Please contact admin to add exercises.");
            }

            System.out.println("✅ [PersonalizationService] Found " + templateDetails.size() + " template details");

            List<PersonalizedPlanDetail> personalizedDetails = new ArrayList<>();

            int successCount = 0;
            int errorCount = 0;
            
            for (TrainingPlanDetail template : templateDetails) {
                try {
                    System.out.println("🔄 [PersonalizationService] Processing template tpdId: " + template.getTpdId() + ", day: " + template.getDayNumber());
                    PersonalizedPlanDetail personalized = createPersonalizedDetail(
                            user,
                            template,
                            healthProfile, // Có thể null nếu không có Health Profile
                            utId // UserTraining ID
                    );
                    personalizedDetails.add(personalized);
                    successCount++;
                    System.out.println("✅ [PersonalizationService] Created personalized detail for day " + template.getDayNumber() + " (success: " + successCount + ", errors: " + errorCount + ")");
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("❌ [PersonalizationService] Error creating personalized detail for template tpdId: " + template.getTpdId());
                    System.err.println("❌ [PersonalizationService] Day: " + template.getDayNumber());
                    System.err.println("❌ [PersonalizationService] Error: " + e.getMessage());
                    System.err.println("❌ [PersonalizationService] Error class: " + e.getClass().getName());
                    e.printStackTrace();
                    // Continue with other templates instead of failing entire operation
                }
            }
            
            System.out.println("📊 [PersonalizationService] Summary: " + successCount + " successful, " + errorCount + " errors out of " + templateDetails.size() + " templates");

            if (personalizedDetails.isEmpty()) {
                System.out.println("⚠️ [PersonalizationService] No personalized details created");
                return new NotificationResponse(false, 
                        "Could not create any personalized details. Please check template and health profile.");
            }

            System.out.println("💾 [PersonalizationService] Saving " + personalizedDetails.size() + " personalized details");
            
            // Delete existing personalized details for this user and training plan first
            // Để tránh duplicate khi regenerate
            try {
                List<PersonalizedPlanDetail> existingDetails = personalizedPlanDetailRepository.findByUser_Id(userId);
                if (!existingDetails.isEmpty()) {
                    // Filter only those belonging to this training plan
                    List<PersonalizedPlanDetail> toDelete = existingDetails.stream()
                            .filter(ppd -> {
                                // Check if this personalized detail belongs to the current training plan
                                // by checking if the challenge exists in the template
                                return templateDetails.stream()
                                        .anyMatch(td -> td.getChallenge() != null && 
                                                td.getChallenge().getId().equals(ppd.getChallenge().getId()));
                            })
                            .collect(java.util.stream.Collectors.toList());
                    
                    if (!toDelete.isEmpty()) {
                        System.out.println("🗑️ [PersonalizationService] Deleting " + toDelete.size() + " existing personalized details");
                        personalizedPlanDetailRepository.deleteAll(toDelete);
                        entityManager.flush();
                    }
                }
            } catch (Exception deleteEx) {
                System.err.println("⚠️ [PersonalizationService] Error deleting existing details (continuing anyway): " + deleteEx.getMessage());
                deleteEx.printStackTrace();
                // Continue even if delete fails - không throw để tránh rollback
            }
            
            // Save all personalized details
            List<PersonalizedPlanDetail> savedDetails;
            try {
                savedDetails = personalizedPlanDetailRepository.saveAll(personalizedDetails);
                // Flush to ensure data is persisted immediately
                entityManager.flush();
                System.out.println("✅ [PersonalizationService] Successfully saved " + savedDetails.size() + " personalized details");
            } catch (Exception saveEx) {
                System.err.println("❌ [PersonalizationService] Error saving personalized details: " + saveEx.getMessage());
                saveEx.printStackTrace();
                // Return error response thay vì throw để tránh "Transaction silently rolled back"
                // Transaction sẽ tự rollback khi method kết thúc (vì có exception trong transaction)
                // Nhưng ta return response thay vì throw để Spring không log warning
                return new NotificationResponse(false, 
                        "Failed to save personalized details: " + saveEx.getMessage());
            }
            
            // Verify saved records for this user
            try {
                Long userSavedCount = (long) personalizedPlanDetailRepository.findByUser_Id(userId).size();
                System.out.println("📊 [PersonalizationService] Total personalized details for userId " + userId + ": " + userSavedCount);
            } catch (Exception verifyEx) {
                System.err.println("⚠️ [PersonalizationService] Error verifying saved records (non-critical): " + verifyEx.getMessage());
                // Non-critical, continue
            }

            return new NotificationResponse(true, 
                    "Personalized plan details created successfully", 
                    personalizedDetails.size());
        } catch (Exception e) {
            System.err.println("❌ [PersonalizationService] Error creating personalized plan: " + e.getMessage());
            System.err.println("❌ [PersonalizationService] Error class: " + e.getClass().getName());
            e.printStackTrace();
            
            // Return error response thay vì throw để tránh "Transaction silently rolled back"
            // Transaction sẽ tự rollback khi method kết thúc (vì có exception trong transaction)
            // Nhưng ta return response thay vì throw để Spring không log warning
            return new NotificationResponse(false, 
                    "Error creating personalized plan: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách bài tập đã cá nhân hóa cho một ngày cụ thể
     * Video URL được lấy từ Challenge entity
     */
    @Override
    public NotificationResponse getPersonalizedDayDetails(Long utId, Integer dayNumber) {
        try {
            UserTraining userTraining = userTrainingRepository.findById(utId)
                    .orElseThrow(() -> new RuntimeException("UserTraining not found"));
            
            Long userId = userTraining.getUser().getId();
            
            List<PersonalizedPlanDetail> personalizedDetails = 
                    personalizedPlanDetailRepository.findByUser_IdAndDayNumber(userId, dayNumber);

            List<PersonalizedPlanDetailResponse> responses = personalizedDetails.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());

            return new NotificationResponse(true, 
                    "Personalized day details retrieved successfully", 
                    responses);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, 
                    "Error retrieving personalized details: " + e.getMessage());
        }
    }

    /**
     * Lấy bài tập cá nhân hóa cho hôm nay (theo userId)
     * GET /personalized/today?userId=123
     */
    public NotificationResponse getTodayPersonalizedWorkout(Long userId, Integer dayNumber) {
        try {
            List<PersonalizedPlanDetail> personalizedDetails = 
                    personalizedPlanDetailRepository.findByUser_IdAndDayNumber(userId, dayNumber);

            List<PersonalizedPlanDetailResponse> responses = personalizedDetails.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());

            return new NotificationResponse(true, 
                    "Today's personalized workout retrieved successfully", 
                    responses);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, 
                    "Error retrieving today's workout: " + e.getMessage());
        }
    }

    /**
     * Thuật toán cá nhân hóa dựa trên Health Profile
     * healthProfile có thể null nếu user chưa có Health Profile
     */
    private PersonalizedPlanDetail createPersonalizedDetail(
            User user,
            TrainingPlanDetail template,
            HealthProfile healthProfile,
            Long utId) {

        Challenges challenge = template.getChallenge();
        if (challenge == null) {
            System.err.println("❌ [PersonalizationService] Challenge is null for template tpdId: " + template.getTpdId());
            throw new RuntimeException("Challenge not found in template tpdId: " + template.getTpdId());
        }
        
        System.out.println("📝 [PersonalizationService] Creating personalized detail for challenge: " + challenge.getTitle());

        // Get default values from template
        Integer defaultReps = template.getReps() != null ? template.getReps() : 10;
        Integer defaultSets = template.getSets() != null ? template.getSets() : 3;
        
        System.out.println("📋 [PersonalizationService] Template values - reps: " + defaultReps + ", sets: " + defaultSets);

        // Calculate personalized reps and sets based on Health Profile
        // Nếu không có Health Profile, dùng default values
        Integer personalizedReps;
        Integer personalizedSets;
        
        if (healthProfile != null) {
            System.out.println("🧮 [PersonalizationService] Calculating personalized values using Health Profile...");
            personalizedReps = calculatePersonalizedReps(defaultReps, healthProfile);
            personalizedSets = calculatePersonalizedSets(defaultSets, healthProfile);
            System.out.println("✅ [PersonalizationService] Personalized values - reps: " + defaultReps + " → " + personalizedReps + ", sets: " + defaultSets + " → " + personalizedSets);
        } else {
            System.out.println("⚠️ [PersonalizationService] No Health Profile found, using template values (NOT personalized)");
            personalizedReps = defaultReps;
            personalizedSets = defaultSets;
        }

        // Get exercise name from challenge
        String exerciseName = challenge.getTitle() != null ? challenge.getTitle() : "Exercise";

        // Get difficulty from challenge
        String difficulty = challenge.getDifficult() != null 
                ? challenge.getDifficult().name() 
                : "MEDIUM";

        // Determine target muscle (có thể lấy từ challenge description hoặc mặc định)
        String targetMuscle = determineTargetMuscle(challenge, exerciseName);
        
        // Validate all required fields
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }
        if (template.getDayNumber() == null) {
            throw new RuntimeException("Day number cannot be null");
        }
        if (challenge == null) {
            throw new RuntimeException("Challenge cannot be null");
        }
        if (exerciseName == null || exerciseName.trim().isEmpty()) {
            exerciseName = "Exercise"; // Default value
        }
        if (personalizedSets == null || personalizedSets <= 0) {
            personalizedSets = 3; // Default value
        }
        if (personalizedReps == null || personalizedReps <= 0) {
            personalizedReps = 10; // Default value
        }
        if (difficulty == null || difficulty.trim().isEmpty()) {
            difficulty = "MEDIUM"; // Default value
        }
        if (targetMuscle == null || targetMuscle.trim().isEmpty()) {
            targetMuscle = "Full Body"; // Default value
        }
        
        // Get user weight for calories calculation
        BigDecimal userWeightKg = null;
        List<InformationBodyUser> bodyInfoList = informationBodyUserRepository.findByUserId(user.getId());
        if (!bodyInfoList.isEmpty()) {
            InformationBodyUser bodyInfo = bodyInfoList.get(0);
            userWeightKg = bodyInfo.getWeightKg();
        }

        // Estimate duration from sets and reps (average 2-3 minutes per set including rest)
        Integer estimatedDurationMinutes = personalizedSets * 3; // 3 minutes per set (including rest)

        // Calculate estimated calories
        Integer estimatedCalories = CaloriesCalculator.calculateCalories(
                challenge.getExerciseType(),
                estimatedDurationMinutes,
                personalizedSets,
                personalizedReps,
                userWeightKg
        );

        System.out.println("🔨 [PersonalizationService] Building PersonalizedPlanDetail: day=" + template.getDayNumber() + 
                ", exercise=" + exerciseName + ", sets=" + personalizedSets + ", reps=" + personalizedReps +
                ", estimatedCalories=" + estimatedCalories);

        PersonalizedPlanDetail result = PersonalizedPlanDetail.builder()
                .tpdId(template.getTpdId()) // ⚠️ QUAN TRỌNG: Set tpdId để reference đến template gốc
                .utId(utId) // ⚠️ QUAN TRỌNG: Set utId để reference đến UserTraining
                .user(user)
                .dayNumber(template.getDayNumber())
                .challenge(challenge)
                .exerciseName(exerciseName)
                .sets(personalizedSets)
                .reps(personalizedReps)
                .difficulty(difficulty)
                .targetMuscle(targetMuscle)
                .estimatedCalories(estimatedCalories) // ✅ Calculate and save estimated calories
                .build();
        
        System.out.println("🔗 [PersonalizationService] Linked to template tpdId: " + template.getTpdId() + ", utId: " + utId);
        
        System.out.println("✅ [PersonalizationService] PersonalizedPlanDetail built successfully");
        return result;
    }

    /**
     * Tính số reps cá nhân hóa dựa trên Health Profile
     * Service phân tích và đưa ra số reps phù hợp với user
     */
    private Integer calculatePersonalizedReps(Integer defaultReps, HealthProfile healthProfile) {
        if (healthProfile == null) {
            return defaultReps;
        }
        
        double multiplier = 1.0;

        // Adjust based on daily activity level
        if (healthProfile.getDailyActivityLevel() != null && !healthProfile.getDailyActivityLevel().trim().isEmpty()) {
            switch (healthProfile.getDailyActivityLevel().toLowerCase()) {
                case "sedentary":
                    multiplier = 0.7; // Giảm 30%
                    break;
                case "lightly_active":
                    multiplier = 0.85; // Giảm 15%
                    break;
                case "moderately_active":
                    multiplier = 1.0; // Giữ nguyên
                    break;
                case "very_active":
                    multiplier = 1.15; // Tăng 15%
                    break;
                case "extra_active":
                    multiplier = 1.3; // Tăng 30%
                    break;
            }
        }

        // Adjust based on workout frequency
        if (healthProfile.getWorkoutFrequencyPerWeek() != null) {
            if (healthProfile.getWorkoutFrequencyPerWeek() < 2) {
                multiplier *= 0.8; // Ít tập → giảm
            } else if (healthProfile.getWorkoutFrequencyPerWeek() > 5) {
                multiplier *= 1.2; // Tập nhiều → tăng
            }
        }

        // Adjust based on BMI (nếu có) - BMI là BigDecimal
        if (healthProfile.getBmi() != null) {
            double bmiValue = healthProfile.getBmi().doubleValue();
            if (bmiValue > 30) {
                multiplier *= 0.8; // Béo phì → giảm
            } else if (bmiValue < 18.5) {
                multiplier *= 0.9; // Gầy → giảm nhẹ
            }
        }

        int personalizedReps = (int) Math.round(defaultReps * multiplier);
        int result = Math.max(1, personalizedReps); // Tối thiểu 1 rep
        
        // Log chi tiết để debug
        System.out.println("📊 [PersonalizationService] Reps calculation:");
        System.out.println("   - Default: " + defaultReps);
        System.out.println("   - Multiplier: " + String.format("%.2f", multiplier));
        System.out.println("   - Result: " + result);
        System.out.println("   - Status: " + (multiplier != 1.0 ? "✅ PERSONALIZED" : "⚠️ SAME AS TEMPLATE (multiplier = 1.0)"));
        
        return result;
    }

    /**
     * Tính số sets cá nhân hóa dựa trên Health Profile
     * Service phân tích và đưa ra số sets phù hợp với user
     */
    private Integer calculatePersonalizedSets(Integer defaultSets, HealthProfile healthProfile) {
        if (healthProfile == null) {
            return defaultSets;
        }
        
        // Tương tự như reps, nhưng điều chỉnh ít hơn
        double multiplier = 1.0;

        if (healthProfile.getDailyActivityLevel() != null && !healthProfile.getDailyActivityLevel().trim().isEmpty()) {
            switch (healthProfile.getDailyActivityLevel().toLowerCase()) {
                case "sedentary":
                    multiplier = 0.8;
                    break;
                case "very_active":
                case "extra_active":
                    multiplier = 1.1;
                    break;
            }
        }

        int personalizedSets = (int) Math.round(defaultSets * multiplier);
        int result = Math.max(1, personalizedSets); // Tối thiểu 1 set
        
        // Log chi tiết để debug
        System.out.println("📊 [PersonalizationService] Sets calculation:");
        System.out.println("   - Default: " + defaultSets);
        System.out.println("   - Multiplier: " + String.format("%.2f", multiplier));
        System.out.println("   - Result: " + result);
        System.out.println("   - Status: " + (multiplier != 1.0 ? "✅ PERSONALIZED" : "⚠️ SAME AS TEMPLATE (multiplier = 1.0)"));
        
        return result;
    }

    /**
     * Lấy tất cả PersonalizedPlanDetail của user (cho admin)
     */
    @Override
    public NotificationResponse getAllPersonalizedPlanDetails(Long userId) {
        try {
            List<PersonalizedPlanDetail> personalizedDetails = 
                    personalizedPlanDetailRepository.findByUser_Id(userId);
            
            List<PersonalizedPlanDetailResponse> responses = personalizedDetails.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            
            return new NotificationResponse(true, 
                    "All personalized plan details retrieved successfully", 
                    responses);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, 
                    "Error retrieving personalized plan details: " + e.getMessage());
        }
    }

    /**
     * Admin cập nhật PersonalizedPlanDetail của user
     */
    @Override
    @Transactional
    public NotificationResponse updatePersonalizedPlanDetail(Long ppdId, PersonalizedPlanDetailResponse request) {
        try {
            PersonalizedPlanDetail ppd = personalizedPlanDetailRepository.findById(ppdId)
                    .orElseThrow(() -> new RuntimeException("PersonalizedPlanDetail not found with id: " + ppdId));
            
            // Update fields
            if (request.getSets() != null) {
                ppd.setSets(request.getSets());
            }
            if (request.getReps() != null) {
                ppd.setReps(request.getReps());
            }
            if (request.getDifficulty() != null) {
                ppd.setDifficulty(request.getDifficulty());
            }
            if (request.getTargetMuscle() != null) {
                ppd.setTargetMuscle(request.getTargetMuscle());
            }
            if (request.getExerciseName() != null) {
                ppd.setExerciseName(request.getExerciseName());
            }
            
            PersonalizedPlanDetail saved = personalizedPlanDetailRepository.save(ppd);
            PersonalizedPlanDetailResponse response = toResponse(saved);
            
            return new NotificationResponse(true, 
                    "Personalized plan detail updated successfully", 
                    response);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, 
                    "Error updating personalized plan detail: " + e.getMessage());
        }
    }

    /**
     * Xác định nhóm cơ mục tiêu dựa trên tên bài tập
     */
    private String determineTargetMuscle(Challenges challenge, String exerciseName) {
        String nameLower = exerciseName.toLowerCase();
        
        if (nameLower.contains("push") || nameLower.contains("chest")) {
            return "Chest";
        } else if (nameLower.contains("squat") || nameLower.contains("leg")) {
            return "Legs";
        } else if (nameLower.contains("pull") || nameLower.contains("back")) {
            return "Back";
        } else if (nameLower.contains("bicep") || nameLower.contains("tricep") || nameLower.contains("arm")) {
            return "Arms";
        } else if (nameLower.contains("plank") || nameLower.contains("crunch") || nameLower.contains("core") || nameLower.contains("abs")) {
            return "Core";
        } else if (nameLower.contains("shoulder")) {
            return "Shoulders";
        }
        
        return "Full Body"; // Default
    }

    /**
     * Convert PersonalizedPlanDetail to Response DTO
     * Video URL được lấy từ Challenge entity
     */
    private PersonalizedPlanDetailResponse toResponse(PersonalizedPlanDetail ppd) {
        PersonalizedPlanDetailResponse response = new PersonalizedPlanDetailResponse();
        
        response.setId(ppd.getId());
        response.setUserId(ppd.getUser().getId());
        response.setDayNumber(ppd.getDayNumber());
        response.setChallengeId(ppd.getChallenge().getId());
        response.setExerciseName(ppd.getExerciseName());
        response.setSets(ppd.getSets());
        response.setReps(ppd.getReps());
        response.setDifficulty(ppd.getDifficulty());
        response.setTargetMuscle(ppd.getTargetMuscle());
        
        // Lấy video URL từ Challenge entity
        Challenges challenge = ppd.getChallenge();
        if (challenge != null) {
            response.setVideoUrl(challenge.getLinkVideos()); // Video từ Challenge
            response.setChallengeName(challenge.getTitle());
        }
        
        // Set estimated calories
        response.setEstimatedCalories(ppd.getEstimatedCalories());
        
        return response;
    }
}
