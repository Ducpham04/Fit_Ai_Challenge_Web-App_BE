package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.TraningPlanDTO.TrainingPlanRequestDTO;
import com.example.FIT_Challenge.DTO.TraningPlanDTO.TrainingPlanResponseDTO;
import com.example.FIT_Challenge.Entity.Goals;
import com.example.FIT_Challenge.Entity.TrainingPlan;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.GoalRepository;
import com.example.FIT_Challenge.repository.TrainingPlanRepository;
import com.example.FIT_Challenge.service.TrainingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final GoalRepository goalRepository;

    @Override
    public NotificationResponse getAllTrainingPlans() {
        List<TrainingPlanResponseDTO> list = trainingPlanRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
        return new NotificationResponse(true, "All training plans retrieved successfully", list);
    }

    @Override
    public NotificationResponse getTrainingPlansByGoalId(Long goalId) {
        List<TrainingPlanResponseDTO> list = trainingPlanRepository.findByGoalId(goalId).stream()
                .map(this::toResponseDto)
                .toList();
        return new NotificationResponse(true, "Training plans for goal ID: " + goalId, list);
    }

    @Override
    public NotificationResponse getTrainingPlanById(Long tpId) {
        Optional<TrainingPlan> planOpt = trainingPlanRepository.findById(tpId);
        if (planOpt.isEmpty()) {
            return new NotificationResponse(false, "Training plan not found with ID: " + tpId);
        }
        return new NotificationResponse(true, "Training plan found", toResponseDto(planOpt.get()));
    }

    @Override
    public NotificationResponse createTrainingPlan(TrainingPlanRequestDTO dto) {
        try {
            Optional<Goals> goalOpt = goalRepository.findById(dto.getGoalId());
            if (goalOpt.isEmpty()) {
                return new NotificationResponse(false, "Goal not found");
            }

            TrainingPlan plan = new TrainingPlan();
            plan.setGoal(goalOpt.get());
            plan.setTitle(dto.getTitle());
            plan.setDescription(dto.getDescription());
            plan.setDifficultyLevel(dto.getDifficultyLevel());
            plan.setDurationWeeks(dto.getDurationWeeks());

            trainingPlanRepository.save(plan);
            return new NotificationResponse(true, "Training plan created successfully", toResponseDto(plan));
        } catch (Exception e) {
            return new NotificationResponse(false, "Error creating training plan: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse updateTrainingPlan(Long tpId, TrainingPlanRequestDTO dto) {
        Optional<TrainingPlan> planOpt = trainingPlanRepository.findById(tpId);
        if (planOpt.isEmpty()) {
            return new NotificationResponse(false, "Training plan not found");
        }

        TrainingPlan plan = planOpt.get();

        if (dto.getGoalId() != null) {
            goalRepository.findById(dto.getGoalId()).ifPresent(plan::setGoal);
        }
        if (dto.getTitle() != null) plan.setTitle(dto.getTitle());
        if (dto.getDescription() != null) plan.setDescription(dto.getDescription());
        if (dto.getDifficultyLevel() != null) plan.setDifficultyLevel(dto.getDifficultyLevel());
        if (dto.getDurationWeeks() != null) plan.setDurationWeeks(dto.getDurationWeeks());

        trainingPlanRepository.save(plan);
        return new NotificationResponse(true, "Training plan updated successfully", toResponseDto(plan));
    }

    @Override
    public NotificationResponse deleteTrainingPlan(Long tpId) {
        if (!trainingPlanRepository.existsById(tpId)) {
            return new NotificationResponse(false, "Training plan not found");
        }
        trainingPlanRepository.deleteById(tpId);
        return new NotificationResponse(true, "Training plan deleted successfully");
    }

    private TrainingPlanResponseDTO toResponseDto(TrainingPlan plan) {
        TrainingPlanResponseDTO dto = new TrainingPlanResponseDTO();
        dto.setTpId(plan.getTpId());
        dto.setGoalId(plan.getGoal() != null ? plan.getGoal().getId() : null);
        dto.setGoalName(plan.getGoal() != null ? plan.getGoal().getName() : null);
        dto.setLinkImage(plan.getGoal()!= null ? plan.getGoal().getImageLink(): null);
        dto.setTitle(plan.getTitle());
        dto.setDescription(plan.getDescription());
        dto.setDifficultyLevel(plan.getDifficultyLevel());
        dto.setDurationWeeks(plan.getDurationWeeks());
        dto.setCreatedAt(plan.getCreatedAt().toString());
        return dto;
    }
}
