package com.example.fitchallenge.DTO.TrainingPlanDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho Exercise trong Training Plan theo yêu cầu FE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseDTO {
    private Long id;
    private String name;
    private Integer sets;
    private Integer reps;
    private Integer duration; // in seconds
    private Integer restTime; // in seconds
    private String instructions;
    private String videoUrl;
}



