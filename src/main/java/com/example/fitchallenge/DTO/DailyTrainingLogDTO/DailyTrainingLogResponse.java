package com.example.fitchallenge.DTO.DailyTrainingLogDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * DTO for DailyTrainingLog response
 * Kết hợp thông tin từ DailyTrainingLog với Challenge để hiển thị
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyTrainingLogResponse {
    
    private Long dtlId;
    private Long userId;
    private Long trainingPlanId;
    private String trainingPlanTitle;
    private LocalDate trainingDate;
    private Integer dayNumber;
    
    // Challenge information
    private Long challengeId;
    private String challengeName;
    private String challengeTitle;
    private String challengeDescription;
    private String difficulty; // EASY, MEDIUM, HARD
    private String videoUrl;
    private String exerciseType; // AI model type
    
    // Status
    private String status; // not_started, in_progress, completed, skipped
    
    // Progress tracking
    private Integer actualDurationMinutes;
    private Integer caloriesBurned;
    private Integer setsCompleted;
    private Integer repsCompleted;
    private Integer targetSets; // From TrainingPlanDetail
    private Integer targetReps; // From TrainingPlanDetail
    
    // AI evaluation
    private Integer score;
    private Double confidence;
    
    // User notes
    private String notes;
    private Integer perceivedDifficulty;
    private Integer effortLevel;
    
    // Timestamps
    private ZonedDateTime startedAt;
    private ZonedDateTime completedAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

