package com.example.fitchallenge.dto.user.userprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Mục tiêu user
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGoalsDTO {
    private Integer weeklyWorkouts; // Số workouts đã completed trong tuần này
    private Integer weeklyWorkoutsTarget; // Target số workouts mỗi tuần (default: 5)
    private Integer dailyCalories; // Recommended calories per day
    private Integer monthlyDistance; // Monthly distance goal (km)
    private String goalName; // Primary goal name (e.g., "Lose Weight", "Build Muscle")
    private Long goalId; // Primary goal ID
}