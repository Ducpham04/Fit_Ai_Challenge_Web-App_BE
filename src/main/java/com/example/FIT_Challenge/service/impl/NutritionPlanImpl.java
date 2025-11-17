package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.Entity.Goals;
import com.example.FIT_Challenge.Entity.NutritionPlan;
import com.example.FIT_Challenge.DTO.NutritionPlanDTO.NutritionPlanRequest;
import com.example.FIT_Challenge.DTO.NutritionPlanDTO.NutritionPlanResponse;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.GoalRepository;
import com.example.FIT_Challenge.repository.NutritionPlanRepository;
import com.example.FIT_Challenge.service.NutritionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionPlanImpl implements NutritionPlanService {

    private final NutritionPlanRepository nutritionPlanRepository;
    private final GoalRepository goalsRepository;

    private NutritionPlanResponse toResponse(NutritionPlan entity) {
        NutritionPlanResponse dto = new NutritionPlanResponse();
        dto.setPlanId(entity.getPlanId());
        dto.setGoalId(entity.getGoal().getId());
        dto.setGoalName(entity.getGoal().getName());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCaloriesPerDay(entity.getCaloriesPerDay());
        dto.setProteinG(entity.getProteinG());
        dto.setCarbsG(entity.getCarbsG());
        dto.setFatG(entity.getFatG());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    @Override
    public NotificationResponse createPlan(NutritionPlanRequest request) {
        try {
            Goals goal = goalsRepository.findById(request.getGoalId())
                    .orElseThrow(() -> new RuntimeException("Goal not found with ID: " + request.getGoalId()));

            NutritionPlan plan = new NutritionPlan();
            plan.setGoal(goal);
            plan.setTitle(request.getTitle());
            plan.setDescription(request.getDescription());
            plan.setCaloriesPerDay(request.getCaloriesPerDay());
            plan.setProteinG(request.getProteinG());
            plan.setCarbsG(request.getCarbsG());
            plan.setFatG(request.getFatG());
            plan.setStatus(request.getStatus() != null ? request.getStatus() : "active");

            NutritionPlan savedPlan = nutritionPlanRepository.save(plan);
            return new NotificationResponse(true, "Nutrition Plan created successfully", toResponse(savedPlan));
        } catch (Exception e) {
            return new NotificationResponse(false, "Error creating Nutrition Plan: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse updatePlan(Long id, NutritionPlanRequest request) {
        try {
            NutritionPlan plan = nutritionPlanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Nutrition Plan not found with ID: " + id));

            if (request.getGoalId() != null) {
                Goals goal = goalsRepository.findById(request.getGoalId())
                        .orElseThrow(() -> new RuntimeException("Goal not found with ID: " + request.getGoalId()));
                plan.setGoal(goal);
            }

            if (request.getTitle() != null) plan.setTitle(request.getTitle());
            if (request.getDescription() != null) plan.setDescription(request.getDescription());
            if (request.getCaloriesPerDay() != null) plan.setCaloriesPerDay(request.getCaloriesPerDay());
            if (request.getProteinG() != null) plan.setProteinG(request.getProteinG());
            if (request.getCarbsG() != null) plan.setCarbsG(request.getCarbsG());
            if (request.getFatG() != null) plan.setFatG(request.getFatG());
            if (request.getStatus() != null) plan.setStatus(request.getStatus());

            NutritionPlan updatedPlan = nutritionPlanRepository.save(plan);
            return new NotificationResponse(true, "Nutrition Plan updated successfully", toResponse(updatedPlan));
        } catch (Exception e) {
            return new NotificationResponse(false, "Error updating Nutrition Plan: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse deletePlan(Long id) {
        try {
            if (!nutritionPlanRepository.existsById(id)) {
                return new NotificationResponse(false, "Nutrition Plan not found with ID: " + id);
            }
            nutritionPlanRepository.deleteById(id);
            return new NotificationResponse(true, "Nutrition Plan deleted successfully");
        } catch (Exception e) {
            return new NotificationResponse(false, "Error deleting Nutrition Plan: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getPlanById(Long id) {
        try {
            NutritionPlan plan = nutritionPlanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Nutrition Plan not found with ID: " + id));
            return new NotificationResponse(true, "Nutrition Plan found", toResponse(plan));
        } catch (Exception e) {
            return new NotificationResponse(false, "Error fetching Nutrition Plan: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getAllPlans() {
        try {
            List<NutritionPlanResponse> list = nutritionPlanRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return new NotificationResponse(true, "All Nutrition Plans retrieved", list);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error fetching Nutrition Plans: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getPlansByGoal(Long goalId) {
        try {
            List<NutritionPlan> plans = nutritionPlanRepository.findByGoalId(goalId);
            if (plans.isEmpty()) {
                return new NotificationResponse(false, "No Nutrition Plans found for Goal ID: " + goalId);
            }
            List<NutritionPlanResponse> list = plans.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return new NotificationResponse(true, "Nutrition Plans for Goal ID: " + goalId, list);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error fetching Nutrition Plans: " + e.getMessage());
        }
    }
}
