package com.example.fitchallenge.DTO.user.userProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO trả về toàn bộ profile
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullUserProfileDTO {
    private UserProfileDTO profile;
    private UserStatsDTO stats;
    private ActivitySummaryDTO activity;
    private List<AchievementDTO> achievements;
    private UserGoalsDTO goals;
    private WeeklyStatsDTO weeklyStats;
}