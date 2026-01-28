package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.dto.goalsdto.goalsDTOpayload;
import com.example.fitchallenge.entity.Goals;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.GoalRepository;
import com.example.fitchallenge.service.FileStorageService;
import com.example.fitchallenge.service.GoalService;
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
    public NotificationResponse update(Long id, goalsDTOpayload dto, MultipartFile image) {

        if (id == null)
            return new NotificationResponse(false, "Goal ID cannot be null");

        Goals existingGoal = goalRepository.findById(id).orElse(null);
        if (existingGoal == null)
            return new NotificationResponse(false, "Goal not found");


        // Check duplicate name
        if (!existingGoal.getName().equalsIgnoreCase(dto.getName())
                && goalRepository.existsByName(dto.getName())) {
            return new NotificationResponse(false, "Goal name already exists");
        }

        // ✔ 1. HANDLE IMAGE UPDATE LOGIC
        if (image != null && !image.isEmpty()) {
            // Upload ảnh mới
            String imagePath = fileChallengeService.uploadFile(image);
            existingGoal.setImageLink(imagePath);
        }
        // Nếu không có ảnh mới → giữ nguyên ảnh cũ, KHÔNG ĐỤNG DTO.getImageLink()

        // ✔ 2. UPDATE TEXT FIELDS SAFELY
        if (dto.getDescription() != null)
            existingGoal.setDescription(dto.getDescription().trim());
        if( dto.getName() != null){
            existingGoal.setName(dto.getName().trim());
        }


        // Save
        goalRepository.save(existingGoal);

        logger.info("Updated goal with ID {}", id);
        return new NotificationResponse(true, "Goal updated successfully", existingGoal);
    }

}
