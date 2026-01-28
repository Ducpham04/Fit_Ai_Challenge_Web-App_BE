package com.example.fitchallenge.dto.user.userprofile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Thống kê nhanh tuần này
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyStatsDTO {
    private Integer workouts;
    private Integer calories;
    private Integer minutes;
}