package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.DashboardDTO;

public interface DashboardService {
    /**
     * Lấy thống kê user
     * @param period day, week, month, year, all
     */
    DashboardDTO.UserStatsResponse getUserStats(String period);
    
    /**
     * Lấy thống kê challenge
     * @param period day, week, month, year, all
     */
    DashboardDTO.ChallengeStatsResponse getChallengeStats(String period);
    
    /**
     * Lấy thống kê training plan
     * @param period day, week, month, year, all
     */
    DashboardDTO.TrainingStatsResponse getTrainingStats(String period);
    
    /**
     * Lấy thống kê nutrition
     * @param period day, week, month, year, all
     */
    DashboardDTO.NutritionStatsResponse getNutritionStats(String period);
    
    /**
     * Lấy thống kê reward
     * @param period day, week, month, year, all
     */
    DashboardDTO.RewardStatsResponse getRewardStats(String period);
    
    /**
     * Lấy tổng quan dashboard
     */
    DashboardDTO.DashboardOverviewResponse getOverview();
}


