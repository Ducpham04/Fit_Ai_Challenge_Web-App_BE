package com.example.fitchallenge.controller.User;

import com.example.fitchallenge.DTO.HealthProfileDTO.HealthProfileRequest;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.HealthProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/health-profile")
@RequiredArgsConstructor
public class HealthProfileController {

    private final HealthProfileService healthProfileService;

    /**
     * Tạo hoặc cập nhật Health Profile
     * Tự động tính toán BMI, BMR, TDEE, Body Fat, Lean Body Mass
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> createOrUpdateHealthProfile(
            @RequestHeader("userId") Long userId,
            @RequestBody HealthProfileRequest request) {
        return ResponseEntity.ok(healthProfileService.createOrUpdateHealthProfile(userId, request));
    }

    /**
     * Lấy Health Profile của user
     */
    @GetMapping
    public ResponseEntity<NotificationResponse> getHealthProfile(
            @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(healthProfileService.getHealthProfile(userId));
    }

    /**
     * Gợi ý Training Plans dựa trên Health Profile
     */
    @GetMapping("/recommended-plans")
    public ResponseEntity<NotificationResponse> getRecommendedTrainingPlans(
            @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(healthProfileService.getRecommendedTrainingPlans(userId));
    }

    /**
     * Sinh Personalized Plan Detail tự động
     */
    @PostMapping("/generate-personal-plan")
    public ResponseEntity<NotificationResponse> generatePersonalizedPlanDetail(
            @RequestHeader("userId") Long userId,
            @RequestParam Long trainingPlanId) {
        return ResponseEntity.ok(healthProfileService.generatePersonalizedPlanDetail(userId, trainingPlanId));
    }
}


