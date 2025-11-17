package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.goalsDTO.goalsDTOpayload;
import com.example.FIT_Challenge.Entity.Goals;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.GoalRepository;
import com.example.FIT_Challenge.service.FileStorageService;
import com.example.FIT_Challenge.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;


import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(GoalServiceImpl.class);

    private final GoalRepository goalRepository;

    private final FileStorageService fileChallengeService;



    @Override
    public List<goalsDTOpayload> getGoals() {


        logger.info("Fetching all goals successfully");
        return goalRepository.findAll().stream()
                .map(goal -> new goalsDTOpayload(
                        goal.getId(),
                        goal.getImageLink(),
                        goal.getName(),
                        goal.getDescription()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponse createGoal(goalsDTOpayload dto, MultipartFile image) {
        if (dto == null) return new NotificationResponse(false, "Goal data cannot be null");
        if (dto.getName() == null || dto.getName().trim().isEmpty())
            return new NotificationResponse(false, "Goal name cannot be empty");
//
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty())
            return new NotificationResponse(false, "Goal description cannot be empty");

        if (goalRepository.existsByName(dto.getName()))
            return new NotificationResponse(false, "Goal name already exists");
        String imagePath =  fileChallengeService.uploadFile(image);
        Goals goals = new Goals();
        goals.setImageLink(imagePath);
        goals.setName(dto.getName().trim());
        goals.setDescription(dto.getDescription().trim());


        goalRepository.save(goals);
        logger.info("Created new goal: {}", goals.getName());
        return new NotificationResponse(true, "Goal created successfully", goals);
    }

    @Override
    public NotificationResponse delete(Long id) {
        if (id == null) return new NotificationResponse(false, "Goal ID cannot be null");
        if (!goalRepository.existsById(id)) return new NotificationResponse(false, "Goal not found");

        goalRepository.deleteById(id);
        logger.info("Deleted goal with ID {}", id);
        return new NotificationResponse(true, "Goal deleted successfully");
    }

    @Override
    public NotificationResponse update(Long id, goalsDTOpayload dto) {
        if (id == null) return new NotificationResponse(false, "Goal ID cannot be null");

        Goals existingGoal = goalRepository.findById(id).orElse(null);
        if (existingGoal == null) return new NotificationResponse(false, "Goal not found");

        if (dto.getName() == null || dto.getName().trim().isEmpty())
            return new NotificationResponse(false, "Goal name cannot be empty");

        if (!existingGoal.getName().equalsIgnoreCase(dto.getName())
                && goalRepository.existsByName(dto.getName()))
            return new NotificationResponse(false, "Goal name already exists");

        existingGoal.setName(dto.getName().trim());
        existingGoal.setImageLink(dto.getImageLink().trim());
        existingGoal.setDescription(dto.getDescription().trim());
        goalRepository.save(existingGoal);

        logger.info("Updated goal with ID {}", id);
        return new NotificationResponse(true, "Goal updated successfully", existingGoal);
    }
}
