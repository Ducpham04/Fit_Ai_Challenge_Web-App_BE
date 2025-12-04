package com.example.fitchallenge.DTO.user.userProfile;

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
    private Integer weeklyWorkouts;
    private Integer dailyCalories;
    private Integer monthlyDistance;
}