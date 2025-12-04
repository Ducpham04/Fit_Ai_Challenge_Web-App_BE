package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.fitchallenge.DTO.UserTrainingDTO.UserTrainingDTO;
import com.example.fitchallenge.Entity.TrainingPlan;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.Entity.UserTraining;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.TrainingPlanRepository;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.repository.UserTrainingRepository;
import com.example.fitchallenge.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTrainingServiceImpl implements UserTrainingService {

    private final UserTrainingRepository userTrainingRepository;
    private final UserRepository userRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    @Override
    public NotificationResponse getUserTrainingDetails(Long userId) {

        List<UserTraining> userTraining = userTrainingRepository
                .findUserTrainingDetailsByUserId(userId) ;


        List<UserTrainingDTO> dto = userTraining.stream()
                .map(this::responseToDTO)
                .collect(Collectors.toList());

        return new NotificationResponse( true,

                "User training details fetched successfully",
                dto
        );
    }

    @Override
    public NotificationResponse createUserTraining(UserRequestDTO res) {
        if(!userRepository.existsById(res.getUserID())){
            return new NotificationResponse(false,
                    "User not found",
                    null
            );
        }
        if(userTrainingRepository.existsByUser_IdAndTrainingPlan_TpId(res.getUserID(), res.getTrainingID())){
            return new NotificationResponse(false,
                    "User training already exists",
                    null
            );
        }
        if(res.getEndDate().isBefore(res.getStartDate())){
            return new NotificationResponse(false,
                    "End date must be after start date",
                    null
            );
        }
        UserTraining ut = new UserTraining();
        User user = userRepository.findById(res.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + res.getUserID()));

        TrainingPlan tp = trainingPlanRepository.findById(res.getTrainingID())
                .orElseThrow(() -> new RuntimeException("TrainingPlan not found with id: " + res.getTrainingID()));

        ut.setUser(user);
        ut.setTrainingPlan(tp);

        ut.setStartDate(res.getStartDate());
        ut.setEndDate(res.getEndDate());
        ut.setCompletionPercentage(0.0); // mặc định
        ut.setStatus("active");
        System.out.println(" chuẩn bị set  ");
        userTrainingRepository.save(ut);



        return new NotificationResponse(true,"User training created successfully",
                res
        );
    }

    /**
     * Convert Entity → DTO (Module tách riêng)
     */
    private UserTrainingDTO responseToDTO(UserTraining userTraining) {
        if (userTraining == null) return null;

        UserTrainingDTO dto = new UserTrainingDTO();

        dto.setId(userTraining.getUtId());
        dto.setName(userTraining.getTrainingPlan().getTitle());
        dto.setStartDate(userTraining.getStartDate());
        dto.setEndDate(userTraining.getEndDate());
        dto.setCompletionPercentage(userTraining.getCompletionPercentage());
        dto.setStatus(userTraining.getStatus());

        return dto;
    }
    
    @Override
    public NotificationResponse startTrainingPlan(Long trainingPlanId, Long userId, String startDate) {
        try {
            // Check if user exists
            if (!userRepository.existsById(userId)) {
                return new NotificationResponse(false, "User not found");
            }
            
            // Check if training plan exists
            TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                    .orElseThrow(() -> new RuntimeException("Training plan not found"));
            
            // Check if user already started this training plan
            if (userTrainingRepository.existsByUser_IdAndTrainingPlan_TpId(userId, trainingPlanId)) {
                return new NotificationResponse(false, "User already started this training plan");
            }
            
            // Parse start date
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            
            // Calculate end date based on duration weeks
            LocalDate end = start.plusWeeks(trainingPlan.getDurationWeeks() != null ? trainingPlan.getDurationWeeks() : 4);
            
            // Create UserTraining
            UserTraining userTraining = new UserTraining();
            userTraining.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found")));
            userTraining.setTrainingPlan(trainingPlan);
            userTraining.setStartDate(start);
            userTraining.setEndDate(end);
            userTraining.setCompletionPercentage(0.0);
            userTraining.setStatus("active");
            
            userTrainingRepository.save(userTraining);
            
            // Return response in FE format
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Training plan started successfully");
            response.put("trainingPlanId", trainingPlanId);
            response.put("userId", userId);
            response.put("startDate", start.toString());
            response.put("endDate", end.toString());
            
            return new NotificationResponse(true, "Training plan started successfully", response);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error starting training plan: " + e.getMessage());
        }
    }
}
