package com.example.fitchallenge.DTO.TrainingPlanDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO cho Training Plan response theo yêu cầu FE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingPlanResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String difficulty; // Beginner, Intermediate, Advanced
    private Integer duration; // in weeks
    private List<ExerciseDTO> exercises;
    private String status; // Active, Completed, Paused
    private Integer progress; // Percentage 0-100
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    
    // Additional fields for admin
    private Long goalId;
    private String goalName;
    private Integer subscribers;
    private Double price;
    private String focusArea;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}



