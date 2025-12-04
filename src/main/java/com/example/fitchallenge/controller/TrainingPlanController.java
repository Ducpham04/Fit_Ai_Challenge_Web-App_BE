package com.example.fitchallenge.controller;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.TrainingPlanService;
import com.example.fitchallenge.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO>> getAllTrainingPlans(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long goalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(page, limit);
        Page<com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO> trainingPlans = trainingPlanService.getAllTrainingPlansForUser(
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
    public ResponseEntity<com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO> getTrainingPlanById(@PathVariable Long id) {
        com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO trainingPlan = trainingPlanService.getTrainingPlanByIdForUser(id);
        return ResponseEntity.ok(trainingPlan);
    }

    /**
     * Start a training plan
     * 
     * @param id Training Plan ID
     * @param request Body với userId và startDate
     * @return NotificationResponse
     */
    @PostMapping("/{id}/start")
    public ResponseEntity<NotificationResponse> startTrainingPlan(
            @PathVariable Long id,
            @RequestBody StartTrainingPlanRequest request) {
        
        NotificationResponse response = userTrainingService.startTrainingPlan(
                id, request.getUserId(), request.getStartDate());
        
        return ResponseEntity.ok(response);
    }
    
    // Inner class for start request
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class StartTrainingPlanRequest {
        private Long userId;
        private String startDate;
    }
}

