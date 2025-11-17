package com.example.FIT_Challenge.controller.Admin;

import com.example.FIT_Challenge.DTO.NutritionPlanDTO.NutritionPlanRequest;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.NutritionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutrition-plans")
@RequiredArgsConstructor
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createPlan(@RequestBody NutritionPlanRequest request) {
        NotificationResponse response = nutritionPlanService.createPlan(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> updatePlan(@PathVariable Long id, @RequestBody NutritionPlanRequest request) {
        NotificationResponse response = nutritionPlanService.updatePlan(id, request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> deletePlan(@PathVariable Long id) {
        NotificationResponse response = nutritionPlanService.deletePlan(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getPlanById(@PathVariable Long id) {
        NotificationResponse response = nutritionPlanService.getPlanById(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }

    @GetMapping
    public ResponseEntity<NotificationResponse> getAllPlans() {
        NotificationResponse response = nutritionPlanService.getAllPlans();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/goal/{goalId}")
    public ResponseEntity<NotificationResponse> getPlansByGoal(@PathVariable Long goalId) {
        NotificationResponse response = nutritionPlanService.getPlansByGoal(goalId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }
}
