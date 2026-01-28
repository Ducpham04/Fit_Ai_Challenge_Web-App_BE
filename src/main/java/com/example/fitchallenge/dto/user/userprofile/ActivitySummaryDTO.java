package com.example.fitchallenge.dto.user.userprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Thống kê hoạt động
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitySummaryDTO {
    private Integer totalCaloriesBurned;
    private Integer totalMinutes;
    private String favoriteWorkout;
}
