package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.UserNutritionDTO.UserNutritionRequest;
import com.example.FIT_Challenge.DTO.UserNutritionDTO.UserNutritionResponse;
import com.example.FIT_Challenge.Entity.NutritionPlan;
import com.example.FIT_Challenge.Entity.User;
import com.example.FIT_Challenge.Entity.UserNutrition;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.NutritionPlanRepository;
import com.example.FIT_Challenge.repository.User.UserRepository;
import com.example.FIT_Challenge.repository.UserNutritionRepository;
import com.example.FIT_Challenge.service.UserNutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNutritionServiceImpl implements UserNutritionService {

    private final UserNutritionRepository userNutritionRepository;
    private final UserRepository userRepository;
    private final NutritionPlanRepository nutritionPlanRepository;

    @Override
    public NotificationResponse createUserNutrition(UserNutritionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        NutritionPlan plan = nutritionPlanRepository.findById(request.getNutritionPlanId())
                .orElseThrow(() -> new RuntimeException("NutritionPlan not found"));

        UserNutrition un = UserNutrition.builder()
                .user(user)
                .nutritionPlan(plan)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : "active")
                .build();
        userNutritionRepository.save(un);

        UserNutritionResponse userNutritionResponse = new UserNutritionResponse();
        userNutritionResponse.setUnId(un.getUnId());
        userNutritionResponse.setUserId(user.getId());
        userNutritionResponse.setUserName(user.getUserName());
        userNutritionResponse.setNutritionPlanId(plan.getPlanId());
        userNutritionResponse.setStartDate(request.getStartDate());
        userNutritionResponse.setEndDate(request.getEndDate());
        userNutritionResponse.setStatus(un.getStatus());

        return new NotificationResponse(true, "UserNutrition created successfully", userNutritionResponse);
    }

    @Override
    public NotificationResponse updateUserNutrition(Long id, UserNutritionRequest request) {
        UserNutrition un = userNutritionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserNutrition not found"));

        if(request.getUserId() != null){
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            un.setUser(user);
        }

        if(request.getNutritionPlanId() != null){
            NutritionPlan plan = nutritionPlanRepository.findById(request.getNutritionPlanId())
                    .orElseThrow(() -> new RuntimeException("NutritionPlan not found"));
            un.setNutritionPlan(plan);
        }

        un.setStartDate(request.getStartDate());
        un.setEndDate(request.getEndDate());
        un.setStatus(request.getStatus());

        userNutritionRepository.save(un);
        UserNutritionResponse userNutritionResponse = new UserNutritionResponse();
        userNutritionResponse.setUnId(un.getUnId());
        userNutritionResponse.setUserId(un.getUser().getId());
        userNutritionResponse.setUserName(un.getUser().getUserName());
        userNutritionResponse.setNutritionPlanId(un.getNutritionPlan().getPlanId());
        userNutritionResponse.setStartDate(request.getStartDate());
        userNutritionResponse.setEndDate(request.getEndDate());
        userNutritionResponse.setStatus(un.getStatus());
        return new NotificationResponse(true, "UserNutrition updated successfully", userNutritionResponse);
    }

    @Override
    public NotificationResponse deleteUserNutrition(Long id) {
        if(!userNutritionRepository.existsById(id)){
            return new NotificationResponse(false, "UserNutrition not found");
        }
        userNutritionRepository.deleteById(id);
        return new NotificationResponse(true, "UserNutrition deleted successfully");
    }

    @Override
    public NotificationResponse getUserNutrition(Long id) {
        return null;
    }

    @Override
    public NotificationResponse getAllUserNutrition() {
        List<UserNutritionResponse> list =  userNutritionRepository.findAll().stream().map(un -> { ;
            UserNutritionResponse response = new UserNutritionResponse();
            response.setUnId(un.getUnId());
            response.setUserId(un.getUser().getId());
            response.setUserName(un.getUser().getUserName());
            response.setNutritionPlanId(un.getNutritionPlan().getPlanId());
            response.setPlanTitle(un.getNutritionPlan().getTitle());
            response.setStartDate(un.getStartDate());
            response.setEndDate(un.getEndDate());
            response.setStatus(un.getStatus());
            return response;
        }).toList();
        return new NotificationResponse(true, "UserNutrition retrieved successfully", list);
    }
}
