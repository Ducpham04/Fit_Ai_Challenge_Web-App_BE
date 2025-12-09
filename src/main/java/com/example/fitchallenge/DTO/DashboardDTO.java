package com.example.fitchallenge.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTOs cho Dashboard Statistics
 */
public class DashboardDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStatsResponse {
        private Long totalUsers;
        private Long activeUsers;
        private Long inactiveUsers;
        private Long bannedUsers;
        private List<DailyUserCount> dailyNewUsers; // Biểu đồ người dùng mới
        private List<DailyActiveUsers> dailyActiveUsers; // Số user active theo ngày
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyUserCount {
        private String date;
        private Long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyActiveUsers {
        private String date;
        private Long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeStatsResponse {
        private Long totalChallenges;
        private Long activeChallenges;
        private Long totalSubmissions;
        private Long completedSubmissions;
        private Long pendingSubmissions;
        private Long failedSubmissions;
        private BigDecimal averageScore; // Điểm trung bình AI
        private BigDecimal averageConfidence; // Độ tin cậy trung bình
        private List<DailyChallengeCount> dailyCompleted; // Số challenge completed theo ngày
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyChallengeCount {
        private String date;
        private Long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrainingStatsResponse {
        private Long totalPlans;
        private Long activePlans;
        private Long totalParticipants;
        private Long completedParticipants;
        private Double averageCompletionRate; // Tỷ lệ hoàn thành trung bình
        private List<PlanCompletionStats> topCompletedPlans;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanCompletionStats {
        private Long planId;
        private String planTitle;
        private Long totalParticipants;
        private Long completedCount;
        private Double completionRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NutritionStatsResponse {
        private Long totalPlans;
        private Long activePlans;
        private Long totalParticipants;
        private BigDecimal averageDailyCalories; // Lượng calories trung bình user tiêu thụ
        private Long totalMealsCompleted; // Số meal completed
        private List<DailyMealCount> dailyMealsCompleted; // Số meal completed theo ngày
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyMealCount {
        private String date;
        private Long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RewardStatsResponse {
        private Long totalRewards;
        private Long activeRewards;
        private Long totalRedemptions;
        private Long pendingRedemptions;
        private Long fulfilledRedemptions;
        private List<TopReward> topRewards; // Top reward được đổi nhiều nhất
        private List<DailyRedemptionCount> dailyRedemptions; // Reward redemption chart
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopReward {
        private Long rewardId;
        private String rewardName;
        private Long redemptionCount;
        private Long totalStock;
        private Long remainingStock;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyRedemptionCount {
        private String date;
        private Long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardOverviewResponse {
        private UserStatsResponse userStats;
        private ChallengeStatsResponse challengeStats;
        private TrainingStatsResponse trainingStats;
        private NutritionStatsResponse nutritionStats;
        private RewardStatsResponse rewardStats;
    }
}

