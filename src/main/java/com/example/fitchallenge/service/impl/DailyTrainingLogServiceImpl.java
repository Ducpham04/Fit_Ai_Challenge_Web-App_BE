package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.DailyTrainingLogDTO.DailyTrainingLogResponse;
import com.example.fitchallenge.Entity.Challenges;
import com.example.fitchallenge.Entity.DailyTrainingLog;
import com.example.fitchallenge.Entity.TrainingPlan;
import com.example.fitchallenge.Entity.TrainingPlanDetail;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.DailyTrainingLogRepository;
import com.example.fitchallenge.repository.TrainingPlanDetailRepository;
import com.example.fitchallenge.repository.TrainingPlanRepository;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.repository.ChallengeRepository;
import com.example.fitchallenge.service.DailyTrainingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyTrainingLogServiceImpl implements DailyTrainingLogService {

    private final DailyTrainingLogRepository dailyTrainingLogRepository;
    private final UserRepository userRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingPlanDetailRepository trainingPlanDetailRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getDailyTrainingLogsByUserAndPlan(Long userId, Long trainingPlanId) {
        try {
            // Verify user and training plan exist
            if (!userRepository.existsById(userId)) {
                throw new RuntimeException("User not found with id: " + userId);
            }
            
            TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                    .orElseThrow(() -> new RuntimeException("Training plan not found with id: " + trainingPlanId));

            // Get all daily training logs for this user and plan
            List<DailyTrainingLog> logs = dailyTrainingLogRepository
                    .findByUser_IdAndTrainingPlan_TpId(userId, trainingPlanId);

            // Get all training plan details (template) for this plan
            List<TrainingPlanDetail> templateDetails = trainingPlanDetailRepository
                    .findByTrainingPlan_TpId(trainingPlanId);

            // Create a map: (dayNumber, challengeId) -> TrainingPlanDetail
            Map<String, TrainingPlanDetail> templateMap = templateDetails.stream()
                    .collect(Collectors.toMap(
                            tpd -> tpd.getDayNumber() + "_" + tpd.getChallenge().getId(),
                            tpd -> tpd,
                            (existing, replacement) -> existing
                    ));

            // Convert logs to response DTOs, combining with template data
            List<DailyTrainingLogResponse> responses = new ArrayList<>();

            // First, process existing logs
            for (DailyTrainingLog log : logs) {
                DailyTrainingLogResponse response = mapToResponse(log, templateMap, trainingPlan);
                responses.add(response);
            }

            // Then, add missing days from template (not_started status)
            for (TrainingPlanDetail template : templateDetails) {
                boolean exists = logs.stream().anyMatch(log ->
                        log.getDayNumber().equals(template.getDayNumber()) &&
                        log.getChallenge() != null &&
                        log.getChallenge().getId().equals(template.getChallenge().getId())
                );

                if (!exists) {
                    // Create a "not_started" log entry from template
                    DailyTrainingLogResponse response = createResponseFromTemplate(template, trainingPlan);
                    responses.add(response);
                }
            }

            // Sort by day number, then by challenge
            responses.sort((a, b) -> {
                int dayCompare = a.getDayNumber().compareTo(b.getDayNumber());
                if (dayCompare != 0) return dayCompare;
                return a.getChallengeId().compareTo(b.getChallengeId());
            });

            return new NotificationResponse(true, 
                    "Daily training logs fetched successfully", 
                    responses);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, 
                    "Error fetching daily training logs: " + e.getMessage(), 
                    null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getDailyTrainingLogsByUserAndPlanAndDay(Long userId, Long trainingPlanId, Integer dayNumber) {
        try {
            List<DailyTrainingLog> logs = dailyTrainingLogRepository
                    .findByUser_IdAndTrainingPlan_TpIdAndDayNumber(userId, trainingPlanId, dayNumber);

            TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                    .orElseThrow(() -> new RuntimeException("Training plan not found"));

            List<TrainingPlanDetail> templateDetails = trainingPlanDetailRepository
                    .findByTrainingPlan_TpIdAndDayNumber(trainingPlanId, dayNumber);

            Map<String, TrainingPlanDetail> templateMap = templateDetails.stream()
                    .collect(Collectors.toMap(
                            tpd -> tpd.getDayNumber() + "_" + tpd.getChallenge().getId(),
                            tpd -> tpd,
                            (existing, replacement) -> existing
                    ));

            List<DailyTrainingLogResponse> responses = new ArrayList<>();

            // Process existing logs
            for (DailyTrainingLog log : logs) {
                responses.add(mapToResponse(log, templateMap, trainingPlan));
            }

            // Add missing challenges from template
            for (TrainingPlanDetail template : templateDetails) {
                boolean exists = logs.stream().anyMatch(log ->
                        log.getChallenge() != null &&
                        log.getChallenge().getId().equals(template.getChallenge().getId())
                );

                if (!exists) {
                    responses.add(createResponseFromTemplate(template, trainingPlan));
                }
            }

            responses.sort((a, b) -> a.getChallengeId().compareTo(b.getChallengeId()));

            return new NotificationResponse(true, 
                    "Daily training logs for day " + dayNumber + " fetched successfully", 
                    responses);
        } catch (Exception e) {
            e.printStackTrace();
            return new NotificationResponse(false, 
                    "Error fetching daily training logs: " + e.getMessage(), 
                    null);
        }
    }

    @Override
    @Transactional
    public NotificationResponse createOrUpdateDailyTrainingLog(
            Long userId, 
            Long trainingPlanId, 
            Integer dayNumber, 
            Long challengeId, 
            String status,
            Integer repsCompleted,
            Integer setsCompleted,
            Integer score,
            Double confidence,
            Integer actualDurationMinutes) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                    .orElseThrow(() -> new RuntimeException("Training plan not found"));

            // Load challenge
            Challenges challenge = challengeRepository.findById(challengeId)
                    .orElseThrow(() -> new RuntimeException("Challenge not found with id: " + challengeId));

            // Try to find existing log
            DailyTrainingLog log = dailyTrainingLogRepository
                    .findByUser_IdAndTrainingPlan_TpIdAndDayNumberAndChallenge_Id(userId, trainingPlanId, dayNumber, challengeId)
                    .orElse(null);

            if (log == null) {
                // Create new log
                log = DailyTrainingLog.builder()
                        .user(user)
                        .trainingPlan(trainingPlan)
                        .challenge(challenge) // ✅ FIX: Set challenge vào log
                        .dayNumber(dayNumber)
                        .trainingDate(LocalDate.now())
                        .status(status)
                        .repsCompleted(repsCompleted) // ✅ FIX: Lưu reps completed
                        .setsCompleted(setsCompleted) // ✅ FIX: Lưu sets completed
                        .score(score) // ✅ FIX: Lưu score
                        .confidence(confidence) // ✅ FIX: Lưu confidence
                        .actualDurationMinutes(actualDurationMinutes) // ✅ FIX: Lưu duration
                        .build();
                
                if ("completed".equals(status)) {
                    log.setCompletedAt(java.time.ZonedDateTime.now());
                }
            } else {
                // Update existing log
                log.setStatus(status);
                if (repsCompleted != null) {
                    log.setRepsCompleted(repsCompleted);
                }
                if (setsCompleted != null) {
                    log.setSetsCompleted(setsCompleted);
                }
                if (score != null) {
                    log.setScore(score);
                }
                if (confidence != null) {
                    log.setConfidence(confidence);
                }
                if (actualDurationMinutes != null) {
                    log.setActualDurationMinutes(actualDurationMinutes);
                }
                if ("completed".equals(status)) {
                    log.setCompletedAt(java.time.ZonedDateTime.now());
                }
            }

            dailyTrainingLogRepository.save(log);
            
            System.out.println("✅ [DailyTrainingLogService] Saved DailyTrainingLog: " + 
                    "userId=" + userId + 
                    ", trainingPlanId=" + trainingPlanId + 
                    ", dayNumber=" + dayNumber + 
                    ", challengeId=" + challengeId + 
                    ", status=" + status +
                    ", repsCompleted=" + repsCompleted +
                    ", setsCompleted=" + setsCompleted);

            return new NotificationResponse(true, 
                    "Daily training log saved successfully", 
                    log);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ [DailyTrainingLogService] Error saving daily training log: " + e.getMessage());
            return new NotificationResponse(false, 
                    "Error saving daily training log: " + e.getMessage(), 
                    null);
        }
    }

    /**
     * Map DailyTrainingLog entity to response DTO, combining with template data
     */
    private DailyTrainingLogResponse mapToResponse(DailyTrainingLog log, Map<String, TrainingPlanDetail> templateMap, TrainingPlan trainingPlan) {
        DailyTrainingLogResponse response = new DailyTrainingLogResponse();
        
        response.setDtlId(log.getDtlId());
        response.setUserId(log.getUser().getId());
        response.setTrainingPlanId(log.getTrainingPlan().getTpId());
        response.setTrainingPlanTitle(trainingPlan.getTitle());
        response.setTrainingDate(log.getTrainingDate());
        response.setDayNumber(log.getDayNumber());
        
        // Challenge information
        if (log.getChallenge() != null) {
            response.setChallengeId(log.getChallenge().getId());
            response.setChallengeName(log.getChallenge().getTitle());
            response.setChallengeTitle(log.getChallenge().getTitle());
            response.setChallengeDescription(log.getChallenge().getDescription());
            response.setDifficulty(log.getChallenge().getDifficult() != null ? log.getChallenge().getDifficult().name() : null);
            response.setVideoUrl(log.getChallenge().getLinkVideos());
            response.setExerciseType(log.getChallenge().getExerciseType());
        }
        
        // Get target sets/reps from template
        String key = log.getDayNumber() + "_" + (log.getChallenge() != null ? log.getChallenge().getId() : "");
        TrainingPlanDetail template = templateMap.get(key);
        if (template != null) {
            response.setTargetSets(template.getSets());
            response.setTargetReps(template.getReps());
        }
        
        // Status and progress
        response.setStatus(log.getStatus());
        response.setActualDurationMinutes(log.getActualDurationMinutes());
        response.setCaloriesBurned(log.getCaloriesBurned());
        response.setSetsCompleted(log.getSetsCompleted());
        response.setRepsCompleted(log.getRepsCompleted());
        
        // AI evaluation
        response.setScore(log.getScore());
        response.setConfidence(log.getConfidence());
        
        // User notes
        response.setNotes(log.getNotes());
        response.setPerceivedDifficulty(log.getPerceivedDifficulty());
        response.setEffortLevel(log.getEffortLevel());
        
        // Timestamps
        response.setStartedAt(log.getStartedAt());
        response.setCompletedAt(log.getCompletedAt());
        response.setCreatedAt(log.getCreatedAt());
        response.setUpdatedAt(log.getUpdatedAt());
        
        return response;
    }

    /**
     * Create response from template (for not_started challenges)
     */
    private DailyTrainingLogResponse createResponseFromTemplate(TrainingPlanDetail template, TrainingPlan trainingPlan) {
        DailyTrainingLogResponse response = new DailyTrainingLogResponse();
        
        response.setDtlId(null); // Not created yet
        response.setTrainingPlanId(trainingPlan.getTpId());
        response.setTrainingPlanTitle(trainingPlan.getTitle());
        response.setDayNumber(template.getDayNumber());
        response.setTrainingDate(LocalDate.now());
        
        // Challenge information from template
        if (template.getChallenge() != null) {
            response.setChallengeId(template.getChallenge().getId());
            response.setChallengeName(template.getChallenge().getTitle());
            response.setChallengeTitle(template.getChallenge().getTitle());
            response.setChallengeDescription(template.getChallenge().getDescription());
            response.setDifficulty(template.getChallenge().getDifficult() != null ? template.getChallenge().getDifficult().name() : null);
            response.setVideoUrl(template.getChallenge().getLinkVideos());
            response.setExerciseType(template.getChallenge().getExerciseType());
        }
        
        // Target from template
        response.setTargetSets(template.getSets());
        response.setTargetReps(template.getReps());
        
        // Default status
        response.setStatus("not_started");
        
        return response;
    }
}

