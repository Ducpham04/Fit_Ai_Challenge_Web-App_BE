package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.TraningPlanDTO.TrainingPlanRequestDTO;
import com.example.fitchallenge.DTO.TraningPlanDTO.TrainingPlanResponseDTO;
import com.example.fitchallenge.DTO.TrainingPlanDTO.ExerciseDTO;
import com.example.fitchallenge.Entity.Challenges;
import com.example.fitchallenge.Entity.Goals;
import com.example.fitchallenge.Entity.TrainingPlan;
import com.example.fitchallenge.Entity.TrainingPlanDetail;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.GoalRepository;
import com.example.fitchallenge.repository.TrainingPlanRepository;
import com.example.fitchallenge.repository.TrainingPlanDetailRepository;
import com.example.fitchallenge.service.TrainingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final GoalRepository goalRepository;
    private final TrainingPlanDetailRepository trainingPlanDetailRepository;

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
    
    @Override
    public Page<com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO> getAllTrainingPlansForUser(String difficulty, String status, Long goalId, Pageable pageable) {
        List<TrainingPlan> allPlans = trainingPlanRepository.findAll();
        
        // Filter by difficulty
        if (difficulty != null && !difficulty.isEmpty()) {
            allPlans = allPlans.stream()
                    .filter(p -> p.getDifficultyLevel() != null && 
                            p.getDifficultyLevel().equalsIgnoreCase(difficulty))
                    .collect(Collectors.toList());
        }
        
        // Filter by goalId
        if (goalId != null) {
            allPlans = allPlans.stream()
                    .filter(p -> p.getGoal() != null && p.getGoal().getId().equals(goalId))
                    .collect(Collectors.toList());
        }
        
        // Map to DTOs with exercises
        List<com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO> dtos = allPlans.stream()
                .map(this::mapToNewTrainingPlanResponseDTO)
                .collect(Collectors.toList());
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO> pagedDtos = dtos.subList(start, end);
        
        return new PageImpl<>(pagedDtos, pageable, dtos.size());
    }
    
    @Override
    public com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO getTrainingPlanByIdForUser(Long tpId) {
        TrainingPlan plan = trainingPlanRepository.findById(tpId)
                .orElseThrow(() -> new RuntimeException("Training plan not found"));
        
        return mapToNewTrainingPlanResponseDTO(plan);
    }
    
    private com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO mapToNewTrainingPlanResponseDTO(TrainingPlan plan) {
        // Get exercises from TrainingPlanDetail
        List<TrainingPlanDetail> details = trainingPlanDetailRepository.findByTrainingPlan_TpId(plan.getTpId());
        List<ExerciseDTO> exercises = details.stream()
                .map(detail -> {
                    Challenges challenge = detail.getChallenge();
                    String videoUrl = challenge != null ? challenge.getLinkVideos() : null;
                    
                    return ExerciseDTO.builder()
                            .id(detail.getTpdId())
                            .name(challenge != null ? challenge.getTitle() : "Exercise")
                            .sets(detail.getSets() != null ? detail.getSets() : 1)
                            .reps(detail.getReps())
                            .duration(detail.getDuration())
                            .restTime(detail.getRestTime())
                            .instructions(detail.getInstructions())
                            .videoUrl(videoUrl)
                            .build();
                })
                .collect(Collectors.toList());
        
        // Map difficulty level
        String difficulty = plan.getDifficultyLevel();
        if (difficulty != null) {
            difficulty = difficulty.toLowerCase();
            if (difficulty.contains("beginner")) difficulty = "Beginner";
            else if (difficulty.contains("intermediate")) difficulty = "Intermediate";
            else if (difficulty.contains("advanced")) difficulty = "Advanced";
        }
        
        return com.example.fitchallenge.DTO.TrainingPlanDTO.TrainingPlanResponseDTO.builder()
                .id(plan.getTpId())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .difficulty(difficulty)
                .duration(plan.getDurationWeeks())
                .exercises(exercises)
                .status("Active") // Default status, can be calculated from UserTraining
                .progress(0) // Default, can be calculated from UserTraining
                .goalId(plan.getGoal() != null ? plan.getGoal().getId() : null)
                .goalName(plan.getGoal() != null ? plan.getGoal().getName() : null)
                .createdAt(plan.getCreatedAt())
                .build();
    }
}
