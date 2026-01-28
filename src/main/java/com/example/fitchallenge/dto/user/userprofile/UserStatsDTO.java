package com.example.fitchallenge.dto.user.userprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Stats chính
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatsDTO {
    private Integer aiScore;
    private Integer challengesCompleted;
    private Integer totalWorkouts;
    private Integer currentStreak;
}