package com.example.fitchallenge.controller.admin;

import com.example.fitchallenge.dto.DashboardDTO;
import com.example.fitchallenge.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/admin/dashboard/user-stats
     * Lấy thống kê user
     */
    @GetMapping("/user-stats")
    public ResponseEntity<DashboardDTO.UserStatsResponse> getUserStats(
            @RequestParam(required = false, defaultValue = "all") String period) {
        return ResponseEntity.ok(dashboardService.getUserStats(period));
    }

    /**
     * GET /api/admin/dashboard/challenge-stats
     * Lấy thống kê challenge
     */
    @GetMapping("/challenge-stats")
    public ResponseEntity<DashboardDTO.ChallengeStatsResponse> getChallengeStats(
            @RequestParam(required = false, defaultValue = "all") String period) {
        return ResponseEntity.ok(dashboardService.getChallengeStats(period));
    }

    /**
     * GET /api/admin/dashboard/training-stats
     * Lấy thống kê training plan
     */
    @GetMapping("/training-stats")
    public ResponseEntity<DashboardDTO.TrainingStatsResponse> getTrainingStats(
            @RequestParam(required = false, defaultValue = "all") String period) {
        return ResponseEntity.ok(dashboardService.getTrainingStats(period));
    }

    /**
     * GET /api/admin/dashboard/nutrition-stats
     * Lấy thống kê nutrition
     */
    @GetMapping("/nutrition-stats")
    public ResponseEntity<DashboardDTO.NutritionStatsResponse> getNutritionStats(
            @RequestParam(required = false, defaultValue = "all") String period) {
        return ResponseEntity.ok(dashboardService.getNutritionStats(period));
    }

    /**
     * GET /api/admin/dashboard/reward-stats
     * Lấy thống kê reward
     */
    @GetMapping("/reward-stats")
    public ResponseEntity<DashboardDTO.RewardStatsResponse> getRewardStats(
            @RequestParam(required = false, defaultValue = "all") String period) {
        return ResponseEntity.ok(dashboardService.getRewardStats(period));
    }

    /**
     * GET /api/admin/dashboard/overview
     * Lấy tổng quan dashboard
     */
    @GetMapping("/overview")
    public ResponseEntity<DashboardDTO.DashboardOverviewResponse> getOverview() {
        return ResponseEntity.ok(dashboardService.getOverview());
    }
}





