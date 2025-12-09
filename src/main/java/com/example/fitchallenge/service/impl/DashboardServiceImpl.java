package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.DashboardDTO;
import com.example.fitchallenge.Entity.*;
import com.example.fitchallenge.repository.*;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final UserTrainingRepository userTrainingRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final UserNutritionRepository userNutritionRepository;
    private final RewardRepository rewardRepository;
    private final RewardRedemptionRepository rewardRedemptionRepository;

    @Override
    public DashboardDTO.UserStatsResponse getUserStats(String period) {
        ZonedDateTime startDate = getStartDate(period);
        
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findAll().stream()
                .filter(u -> "active".equalsIgnoreCase(u.getStatus()))
                .count();
        long inactiveUsers = userRepository.findAll().stream()
                .filter(u -> "inactive".equalsIgnoreCase(u.getStatus()))
                .count();
        long bannedUsers = userRepository.findAll().stream()
                .filter(u -> "banned".equalsIgnoreCase(u.getStatus()))
                .count();

        // Daily new users
        List<DashboardDTO.DailyUserCount> dailyNewUsers = new ArrayList<>();
        if (startDate != null) {
            List<User> users = userRepository.findAll().stream()
                    .filter(u -> u.getCreateAt() != null && 
                            ZonedDateTime.ofInstant(u.getCreateAt().toInstant(), 
                                    java.time.ZoneId.systemDefault()).isAfter(startDate))
                    .collect(Collectors.toList());
            
            Map<String, Long> dailyCount = users.stream()
                    .collect(Collectors.groupingBy(
                            u -> ZonedDateTime.ofInstant(u.getCreateAt().toInstant(), 
                                    java.time.ZoneId.systemDefault())
                                    .toLocalDate().toString(),
                            Collectors.counting()
                    ));
            
            dailyCount.forEach((date, count) -> 
                    dailyNewUsers.add(new DashboardDTO.DailyUserCount(date, count)));
        }

        // Daily active users (users who logged in)
        List<DashboardDTO.DailyActiveUsers> dailyActiveUsers = new ArrayList<>();
        if (startDate != null) {
            List<User> activeUsersList = userRepository.findAll().stream()
                    .filter(u -> u.getLastLoginAt() != null && 
                            ZonedDateTime.ofInstant(u.getLastLoginAt().toInstant(), 
                                    java.time.ZoneId.systemDefault()).isAfter(startDate))
                    .collect(Collectors.toList());
            
            Map<String, Long> dailyActiveCount = activeUsersList.stream()
                    .collect(Collectors.groupingBy(
                            u -> ZonedDateTime.ofInstant(u.getLastLoginAt().toInstant(), 
                                    java.time.ZoneId.systemDefault())
                                    .toLocalDate().toString(),
                            Collectors.counting()
                    ));
            
            dailyActiveCount.forEach((date, count) -> 
                    dailyActiveUsers.add(new DashboardDTO.DailyActiveUsers(date, count)));
        }

        return new DashboardDTO.UserStatsResponse(
                totalUsers, activeUsers, inactiveUsers, bannedUsers,
                dailyNewUsers, dailyActiveUsers
        );
    }

    @Override
    public DashboardDTO.ChallengeStatsResponse getChallengeStats(String period) {
        ZonedDateTime startDate = getStartDate(period);
        
        long totalChallenges = challengeRepository.count();
        long activeChallenges = challengeRepository.findAll().stream()
                .filter(c -> c.getStatus() != null && 
                        c.getStatus() == com.example.fitchallenge.Entity.Challenges.Status.ACTIVE)
                .count();
        
        List<UserChallenge> allSubmissions = userChallengeRepository.findAll();
        if (startDate != null) {
            allSubmissions = allSubmissions.stream()
                    .filter(uc -> uc.getSubmittedAt() != null && 
                            uc.getSubmittedAt().isAfter(startDate))
                    .collect(Collectors.toList());
        }
        
        long totalSubmissions = allSubmissions.size();
        long completedSubmissions = allSubmissions.stream()
                .filter(uc -> "success".equalsIgnoreCase(uc.getStatus()))
                .count();
        long pendingSubmissions = allSubmissions.stream()
                .filter(uc -> "pending".equalsIgnoreCase(uc.getStatus()))
                .count();
        long failedSubmissions = allSubmissions.stream()
                .filter(uc -> "failed".equalsIgnoreCase(uc.getStatus()))
                .count();

        // Average score
        BigDecimal averageScore = allSubmissions.stream()
                .filter(uc -> uc.getScore() != null)
                .map(uc -> BigDecimal.valueOf(uc.getScore()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(
                        allSubmissions.stream().filter(uc -> uc.getScore() != null).count()),
                       2, RoundingMode.HALF_UP);

        // Average confidence
        BigDecimal averageConfidence = allSubmissions.stream()
                .filter(uc -> uc.getConfidence() != null)
                .map(uc -> BigDecimal.valueOf(uc.getConfidence()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(
                        allSubmissions.stream().filter(uc -> uc.getConfidence() != null).count()),
                       2, RoundingMode.HALF_UP);

        // Daily completed challenges
        List<DashboardDTO.DailyChallengeCount> dailyCompleted = new ArrayList<>();
        Map<String, Long> dailyCount = allSubmissions.stream()
                .filter(uc -> "success".equalsIgnoreCase(uc.getStatus()) && 
                        uc.getCompletedAt() != null)
                .collect(Collectors.groupingBy(
                        uc -> uc.getCompletedAt().toLocalDate().toString(),
                        Collectors.counting()
                ));
        
        dailyCount.forEach((date, count) -> 
                dailyCompleted.add(new DashboardDTO.DailyChallengeCount(date, count)));

        return new DashboardDTO.ChallengeStatsResponse(
                totalChallenges, activeChallenges, totalSubmissions,
                completedSubmissions, pendingSubmissions, failedSubmissions,
                averageScore, averageConfidence, dailyCompleted
        );
    }

    @Override
    public DashboardDTO.TrainingStatsResponse getTrainingStats(String period) {
        ZonedDateTime startDate = getStartDate(period);
        
        long totalPlans = trainingPlanRepository.count();
        long activePlans = trainingPlanRepository.findAll().stream()
                .filter(tp -> tp.getCreatedAt() != null)
                .count(); // Có thể thêm status field nếu cần
        
        List<UserTraining> allUserTrainings = userTrainingRepository.findAll();
        if (startDate != null) {
            allUserTrainings = allUserTrainings.stream()
                    .filter(ut -> ut.getStartDate() != null && 
                            ut.getStartDate().atStartOfDay()
                                    .isAfter(startDate.toLocalDateTime()))
                    .collect(Collectors.toList());
        }
        
        long totalParticipants = allUserTrainings.size();
        long completedParticipants = allUserTrainings.stream()
                .filter(ut -> "completed".equalsIgnoreCase(ut.getStatus()))
                .count();

        // Average completion rate
        double averageCompletionRate = allUserTrainings.stream()
                .filter(ut -> ut.getCompletionPercentage() != null)
                .mapToDouble(UserTraining::getCompletionPercentage)
                .average()
                .orElse(0.0);

        // Top completed plans
        Map<Long, List<UserTraining>> planGroups = allUserTrainings.stream()
                .filter(ut -> ut.getTrainingPlan() != null)
                .collect(Collectors.groupingBy(
                        ut -> ut.getTrainingPlan().getTpId()
                ));

        List<DashboardDTO.PlanCompletionStats> topCompletedPlans = planGroups.entrySet().stream()
                .map(entry -> {
                    Long planId = entry.getKey();
                    List<UserTraining> trainings = entry.getValue();
                    TrainingPlan plan = trainings.get(0).getTrainingPlan();
                    
                    long completed = trainings.stream()
                            .filter(ut -> "completed".equalsIgnoreCase(ut.getStatus()))
                            .count();
                    
                    double completionRate = trainings.isEmpty() ? 0.0 :
                            (double) completed / trainings.size() * 100;
                    
                    return new DashboardDTO.PlanCompletionStats(
                            planId,
                            plan.getTitle(),
                            (long) trainings.size(),
                            completed,
                            completionRate
                    );
                })
                .sorted((a, b) -> Double.compare(b.getCompletionRate(), a.getCompletionRate()))
                .limit(10)
                .collect(Collectors.toList());

        return new DashboardDTO.TrainingStatsResponse(
                totalPlans, activePlans, totalParticipants, completedParticipants,
                averageCompletionRate, topCompletedPlans
        );
    }

    @Override
    public DashboardDTO.NutritionStatsResponse getNutritionStats(String period) {
        ZonedDateTime startDate = getStartDate(period);
        
        long totalPlans = nutritionPlanRepository.count();
        long activePlans = nutritionPlanRepository.findAll().stream()
                .filter(np -> "active".equalsIgnoreCase(np.getStatus()))
                .count();
        
        List<UserNutrition> allUserNutritions = userNutritionRepository.findAll();
        if (startDate != null) {
            allUserNutritions = allUserNutritions.stream()
                    .filter(un -> un.getStartDate() != null && 
                            un.getStartDate().atStartOfDay()
                                    .isAfter(startDate.toLocalDateTime()))
                    .collect(Collectors.toList());
        }
        
        long totalParticipants = allUserNutritions.size();
        
        // Average daily calories (từ InformationBodyUser)
        List<User> allUsers = userRepository.findAll();
        List<BigDecimal> caloriesList = allUsers.stream()
                .filter(u -> u.getInformationbodyuser() != null && 
                        !u.getInformationbodyuser().isEmpty())
                .map(u -> u.getInformationbodyuser().get(0).getRecommendedCalories())
                .filter(cal -> cal != null && cal.compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        
        BigDecimal averageDailyCalories = caloriesList.isEmpty() ? BigDecimal.ZERO :
                caloriesList.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(caloriesList.size()), 2, RoundingMode.HALF_UP);

        // Total meals completed (có thể tính từ meal logs nếu có)
        long totalMealsCompleted = 0; // TODO: Tính từ meal completion logs nếu có

        // Daily meals completed
        List<DashboardDTO.DailyMealCount> dailyMealsCompleted = new ArrayList<>();
        // TODO: Implement khi có meal completion tracking

        return new DashboardDTO.NutritionStatsResponse(
                totalPlans, activePlans, totalParticipants,
                averageDailyCalories, totalMealsCompleted, dailyMealsCompleted
        );
    }

    @Override
    public DashboardDTO.RewardStatsResponse getRewardStats(String period) {
        ZonedDateTime startDate = getStartDate(period);
        
        long totalRewards = rewardRepository.count();
        long activeRewards = rewardRepository.findAll().stream()
                .filter(r -> "active".equalsIgnoreCase(r.getStatus()))
                .count();
        
        List<RewardRedemption> allRedemptions = rewardRedemptionRepository.findAll();
        if (startDate != null) {
            allRedemptions = allRedemptions.stream()
                    .filter(rr -> rr.getCreatedAt() != null && 
                            rr.getCreatedAt().isAfter(startDate))
                    .collect(Collectors.toList());
        }
        
        long totalRedemptions = allRedemptions.size();
        long pendingRedemptions = allRedemptions.stream()
                .filter(rr -> "pending".equalsIgnoreCase(rr.getStatus()))
                .count();
        long fulfilledRedemptions = allRedemptions.stream()
                .filter(rr -> "fulfilled".equalsIgnoreCase(rr.getStatus()))
                .count();

        // Top rewards
        Map<Long, List<RewardRedemption>> rewardGroups = allRedemptions.stream()
                .filter(rr -> rr.getReward() != null)
                .collect(Collectors.groupingBy(
                        rr -> rr.getReward().getRewardId()
                ));

        List<DashboardDTO.TopReward> topRewards = rewardGroups.entrySet().stream()
                .map(entry -> {
                    Long rewardId = entry.getKey();
                    List<RewardRedemption> redemptions = entry.getValue();
                    Reward reward = redemptions.get(0).getReward();
                    
                    long redemptionCount = redemptions.size();
                    long totalStock = reward.getStock() != null ? reward.getStock() : 0;
                    long remainingStock = totalStock - redemptionCount;
                    
                    return new DashboardDTO.TopReward(
                            rewardId,
                            reward.getName(),
                            redemptionCount,
                            totalStock,
                            remainingStock
                    );
                })
                .sorted((a, b) -> Long.compare(b.getRedemptionCount(), a.getRedemptionCount()))
                .limit(10)
                .collect(Collectors.toList());

        // Daily redemptions
        List<DashboardDTO.DailyRedemptionCount> dailyRedemptions = new ArrayList<>();
        Map<String, Long> dailyCount = allRedemptions.stream()
                .filter(rr -> rr.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        rr -> rr.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()
                ));
        
        dailyCount.forEach((date, count) -> 
                dailyRedemptions.add(new DashboardDTO.DailyRedemptionCount(date, count)));

        return new DashboardDTO.RewardStatsResponse(
                totalRewards, activeRewards, totalRedemptions,
                pendingRedemptions, fulfilledRedemptions,
                topRewards, dailyRedemptions
        );
    }

    @Override
    public DashboardDTO.DashboardOverviewResponse getOverview() {
        return new DashboardDTO.DashboardOverviewResponse(
                getUserStats("all"),
                getChallengeStats("all"),
                getTrainingStats("all"),
                getNutritionStats("all"),
                getRewardStats("all")
        );
    }

    /**
     * Tính start date dựa trên period
     */
    private ZonedDateTime getStartDate(String period) {
        if (period == null || "all".equalsIgnoreCase(period)) {
            return null; // Lấy tất cả
        }
        
        ZonedDateTime now = ZonedDateTime.now();
        switch (period.toLowerCase()) {
            case "day":
                return now.minusDays(1);
            case "week":
                return now.minusWeeks(1);
            case "month":
                return now.minusMonths(1);
            case "year":
                return now.minusYears(1);
            default:
                return null;
        }
    }
}

