package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.FIT_Challenge.DTO.UserTrainingDTO.UserTrainingDTO;
import com.example.FIT_Challenge.Entity.TrainingPlan;
import com.example.FIT_Challenge.Entity.User;
import com.example.FIT_Challenge.Entity.UserTraining;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.RoleRepository;
import com.example.FIT_Challenge.repository.TrainingPlanRepository;
import com.example.FIT_Challenge.repository.User.UserRepository;
import com.example.FIT_Challenge.repository.UserTrainingRepository;
import com.example.FIT_Challenge.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
