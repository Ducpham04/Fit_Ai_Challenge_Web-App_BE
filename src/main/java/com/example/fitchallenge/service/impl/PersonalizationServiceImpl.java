package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.PersonalizedPlanDetailDTO.PersonalizedPlanDetailResponse;
import com.example.fitchallenge.Entity.*;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.*;
import com.example.fitchallenge.service.PersonalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalizationServiceImpl implements PersonalizationService {

    private final PersonalizedPlanDetailRepository personalizedPlanDetailRepository;
    private final UserTrainingRepository userTrainingRepository;
    private final TrainingPlanDetailRepository trainingPlanDetailRepository;
    private final UserBodyProfileRepository userBodyProfileRepository;

    @Override
    public NotificationResponse createPersonalizedPlanDetails(Long utId) {
        try {
            UserTraining userTraining = userTrainingRepository.findById(utId)
                    .orElseThrow(() -> new RuntimeException("UserTraining not found"));

            // Get user body profile
            Optional<UserBodyProfile> bodyProfileOpt = userBodyProfileRepository.findByUser_Id(
                    userTraining.getUser().getId()
            );

            if (bodyProfileOpt.isEmpty()) {
                return new NotificationResponse(false, 
                        "User body profile not found. Please complete your body profile first.");
            }

            UserBodyProfile bodyProfile = bodyProfileOpt.get();

            // Get all training plan details
            Long trainingPlanId = userTraining.getTrainingPlan().getTpId();
            List<TrainingPlanDetail> templateDetails = trainingPlanDetailRepository
                    .findByTrainingPlan_TpId(trainingPlanId);

            List<PersonalizedPlanDetail> personalizedDetails = new ArrayList<>();

            for (TrainingPlanDetail template : templateDetails) {
                PersonalizedPlanDetail personalized = createPersonalizedDetail(
                        userTraining, 
                        template, 
                        bodyProfile
                );
                personalizedDetails.add(personalized);
            }

            personalizedPlanDetailRepository.saveAll(personalizedDetails);

            return new NotificationResponse(true, 
                    "Personalized plan details created successfully", 
                    personalizedDetails.size());
        } catch (Exception e) {
            return new NotificationResponse(false, 
                    "Error creating personalized plan: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getPersonalizedDayDetails(Long utId, Integer dayNumber) {
        try {
            List<PersonalizedPlanDetail> personalizedDetails = 
                    personalizedPlanDetailRepository.findByUserTrainingAndDayNumber(utId, dayNumber);

            List<PersonalizedPlanDetailResponse> responses = personalizedDetails.stream()
                    .map(this::toResponse)
                    .toList();

            return new NotificationResponse(true, 
                    "Personalized day details retrieved successfully", 
                    responses);
        } catch (Exception e) {
            return new NotificationResponse(false, 
                    "Error retrieving personalized details: " + e.getMessage());
        }
    }

    /**
     * Thuật toán cá nhân hóa dựa trên rule-based
     */
    private PersonalizedPlanDetail createPersonalizedDetail(
            UserTraining userTraining,
            TrainingPlanDetail template,
            UserBodyProfile bodyProfile) {

        PersonalizedPlanDetail personalized = new PersonalizedPlanDetail();
        personalized.setUserTraining(userTraining);
        personalized.setTrainingPlanDetail(template);

        // Get default values
        Integer defaultReps = template.getReps() != null ? template.getReps() : 0;
        Integer defaultDuration = template.getDuration() != null ? template.getDuration() : 0;

        // 1. Goal-based multiplier
        double goalMultiplier = getGoalMultiplier(bodyProfile.getGoal());
        
        // 2. Experience level multiplier
        double experienceMultiplier = getExperienceMultiplier(bodyProfile.getExperienceLevel());
        
        // 3. BMI-based multiplier
        double bmiMultiplier = getBMIMultiplier(bodyProfile.getBmi());
        
        // 4. Injury-based modifications
        String exerciseVariant = getExerciseVariant(template, bodyProfile.getInjuryNotes());
        Integer intensityLevel = calculateIntensityLevel(bodyProfile);

        // Calculate custom reps
        double customRepsDouble = defaultReps * goalMultiplier * experienceMultiplier * bmiMultiplier;
        Integer customReps = (int) Math.round(customRepsDouble);

        // Calculate custom time (for time-based exercises)
        double customTimeDouble = defaultDuration * goalMultiplier * experienceMultiplier;
        Integer customTime = defaultDuration > 0 ? (int) Math.round(customTimeDouble) : null;

        personalized.setCustomReps(customReps);
        personalized.setCustomTime(customTime);
        personalized.setExerciseVariant(exerciseVariant);
        personalized.setIntensityLevel(intensityLevel);

        return personalized;
    }

    /**
     * 3.1 Dựa theo Goal
     */
    private double getGoalMultiplier(String goal) {
        if (goal == null) return 1.0;
        
        return switch (goal.toLowerCase()) {
            case "lose_weight" -> 0.8; // Giảm reps để tăng thời gian
            case "build_muscle" -> 1.2; // Tăng reps
            case "maintain_fitness" -> 1.0; // Giữ nguyên
            default -> 1.0;
        };
    }

    /**
     * 3.2 Dựa theo Experience Level
     */
    private double getExperienceMultiplier(String experienceLevel) {
        if (experienceLevel == null) return 1.0;
        
        return switch (experienceLevel.toLowerCase()) {
            case "beginner" -> 0.7;
            case "intermediate" -> 1.0;
            case "advanced" -> 1.3;
            default -> 1.0;
        };
    }

    /**
     * 3.3 Dựa theo BMI
     */
    private double getBMIMultiplier(Double bmi) {
        if (bmi == null) return 1.0;
        
        if (bmi < 18.5) {
            return 0.8; // Gầy: giảm 20%
        } else if (bmi >= 18.5 && bmi < 25) {
            return 1.0; // Bình thường: giữ nguyên
        } else if (bmi >= 25 && bmi < 30) {
            return 0.9; // Thừa cân: giảm 10%
        } else {
            return 0.7; // Béo phì: giảm mạnh
        }
    }

    /**
     * 3.4 Injury-based personalization
     */
    private String getExerciseVariant(TrainingPlanDetail template, String injuryNotes) {
        if (injuryNotes == null || injuryNotes.isEmpty()) {
            return null;
        }

        String challengeName = template.getChallenge() != null 
                ? template.getChallenge().getTitle().toLowerCase() 
                : "";

        String notesLower = injuryNotes.toLowerCase();

        // Knee injuries
        if (notesLower.contains("knee")) {
            if (challengeName.contains("squat")) {
                return "wall squat";
            }
            if (challengeName.contains("lunge")) {
                return "reverse lunge (low impact)";
            }
        }

        // Back injuries
        if (notesLower.contains("back")) {
            if (challengeName.contains("deadlift")) {
                return "glute bridge";
            }
            if (challengeName.contains("plank")) {
                return "knee plank";
            }
        }

        // Shoulder injuries
        if (notesLower.contains("shoulder")) {
            if (challengeName.contains("push-up") || challengeName.contains("pushup")) {
                return "knee push-up";
            }
        }

        return null;
    }

    /**
     * Calculate intensity level (1-10) based on user profile
     */
    private Integer calculateIntensityLevel(UserBodyProfile bodyProfile) {
        int baseIntensity = 5; // Default

        // Adjust based on experience
        if ("beginner".equalsIgnoreCase(bodyProfile.getExperienceLevel())) {
            baseIntensity = 3;
        } else if ("intermediate".equalsIgnoreCase(bodyProfile.getExperienceLevel())) {
            baseIntensity = 6;
        } else if ("advanced".equalsIgnoreCase(bodyProfile.getExperienceLevel())) {
            baseIntensity = 8;
        }

        // Adjust based on BMI
        if (bodyProfile.getBmi() != null) {
            if (bodyProfile.getBmi() > 30) {
                baseIntensity = Math.max(1, baseIntensity - 2); // Reduce for obesity
            } else if (bodyProfile.getBmi() < 18.5) {
                baseIntensity = Math.max(1, baseIntensity - 1); // Reduce for underweight
            }
        }

        return Math.min(10, Math.max(1, baseIntensity));
    }

    private PersonalizedPlanDetailResponse toResponse(PersonalizedPlanDetail ppd) {
        PersonalizedPlanDetailResponse response = new PersonalizedPlanDetailResponse();
        response.setPpdId(ppd.getPpdId());
        response.setUtId(ppd.getUserTraining().getUtId());
        response.setTpdId(ppd.getTrainingPlanDetail().getTpdId());
        response.setDayNumber(ppd.getTrainingPlanDetail().getDayNumber());
        
        if (ppd.getTrainingPlanDetail().getChallenge() != null) {
            response.setChallengeName(ppd.getTrainingPlanDetail().getChallenge().getTitle());
        }
        
        response.setDefaultReps(ppd.getTrainingPlanDetail().getReps());
        response.setDefaultSets(ppd.getTrainingPlanDetail().getSets());
        response.setDefaultDuration(ppd.getTrainingPlanDetail().getDuration());
        response.setCustomReps(ppd.getCustomReps());
        response.setCustomTime(ppd.getCustomTime());
        response.setCustomDistance(ppd.getCustomDistance());
        response.setExerciseVariant(ppd.getExerciseVariant());
        response.setIntensityLevel(ppd.getIntensityLevel());
        response.setNote(ppd.getNote());
        
        return response;
    }
}

