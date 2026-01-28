package com.example.fitchallenge.controller;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.TrainingPlanDetailService;
import com.example.fitchallenge.service.TrainingPlanService;
import com.example.fitchallenge.service.UserService;
import com.example.fitchallenge.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller cho Training Plan APIs dành cho user (non-admin)
 */
@RestController
@RequestMapping("/api/training-plans")
@RequiredArgsConstructor
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;
    private final UserTrainingService userTrainingService;
    private final UserService userService;
    private final TrainingPlanDetailService trainingPlanDetailService;

    /**
     * Get all training plans với pagination và filters
     * 
     * @param difficulty Filter by difficulty (Beginner, Intermediate, Advanced)
     * @param status Filter by status (Active, Completed, Paused)
     * @param goalId Filter by goal ID
     * @param page Page number (default: 0)
     * @param limit Items per page (default: 10)
     * @return Page of TrainingPlanResponseDTO
     */
    @GetMapping
    public ResponseEntity<Page<com.example.fitchallenge.dto.trainingplandto.TrainingPlanResponseDTO>> getAllTrainingPlans(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long goalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(page, limit);
        Page<com.example.fitchallenge.dto.trainingplandto.TrainingPlanResponseDTO> trainingPlans = trainingPlanService.getAllTrainingPlansForUser(
                difficulty, status, goalId, pageable);
        
        return ResponseEntity.ok(trainingPlans);
    }

    /**
     * Get training plan by ID với full details và exercises array
     * 
     * @param id Training Plan ID
     * @return TrainingPlanResponseDTO với exercises array
     */
    @GetMapping("/{id}")
    public ResponseEntity<com.example.fitchallenge.dto.trainingplandto.TrainingPlanResponseDTO> getTrainingPlanById(@PathVariable Long id) {
        com.example.fitchallenge.dto.trainingplandto.TrainingPlanResponseDTO trainingPlan = trainingPlanService.getTrainingPlanByIdForUser(id);
        return ResponseEntity.ok(trainingPlan);
    }

    /**
     * Get training plan details by plan ID (for user)
     * Returns list of TrainingPlanDetailResponse grouped by day
     * 
     * @param id Training Plan ID
     * @return NotificationResponse with list of TrainingPlanDetailResponse
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<NotificationResponse> getTrainingPlanDetails(@PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanDetailService.getDetailsByPlanId(id));
    }

    /**
     * Start a training plan (with personalization)
     * 
     * @param id Training Plan ID
     * @param request Body với startDate (userId từ JWT)
     * @return NotificationResponse
     */
    @PostMapping("/{id}/start")
    public ResponseEntity<NotificationResponse> startTrainingPlan(
            @PathVariable Long id,
            @RequestBody(required = false) StartTrainingPlanRequest request) {
        
        try {
            // Get current user ID from JWT (auth.getName() returns email)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                return ResponseEntity.ok(new NotificationResponse(false, "Unauthorized: No authentication found"));
            }
            
            // Get userId from email
            Long userId = userService.getUserByEmail(auth.getName()).getId();
            
            // Use provided startDate or default to today
            String startDate = (request != null && request.getStartDate() != null) 
                    ? request.getStartDate() 
                    : java.time.LocalDate.now().toString();
            
            NotificationResponse response = userTrainingService.startTrainingPlan(
                    id, userId, startDate);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }
    
    /**
     * Delete user's training plan
     * Chỉ user sở hữu mới có thể xóa
     * Xóa cascade: PersonalizedPlanDetail, DailyTrainingLog
     * 
     * @param utId UserTraining ID (không phải trainingPlanId)
     * @return NotificationResponse
     */
    @DeleteMapping("/user/{utId}")
    public ResponseEntity<NotificationResponse> deleteUserTraining(@PathVariable Long utId) {
        try {
            // Get current user ID from JWT
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                return ResponseEntity.status(401).body(
                    new NotificationResponse(false, "Unauthorized: No authentication found")
                );
            }
            
            // Get userId from email
            Long userId = userService.getUserByEmail(auth.getName()).getId();
            
            // Delete training plan (service sẽ kiểm tra quyền sở hữu)
            NotificationResponse response = userTrainingService.deleteUserTraining(utId, userId);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(403).body(response); // 403 Forbidden nếu không có quyền
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }
    
    // Inner class for start request
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class StartTrainingPlanRequest {
        private String startDate; // userId now comes from JWT
    }
}

