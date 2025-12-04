package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.MealDTO.MealResponse;
import com.example.fitchallenge.DTO.UserNutritionDTO.UserNutritionRequest;
import com.example.fitchallenge.DTO.UserNutritionDTO.UserNutritionResponse;
import com.example.fitchallenge.Entity.NutritionPlan;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.Entity.UserNutrition;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.NutritionPlanRepository;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.repository.UserNutritionRepository;
import com.example.fitchallenge.service.MealService;
import com.example.fitchallenge.service.UserNutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserNutritionServiceImpl implements UserNutritionService {

    private final UserNutritionRepository userNutritionRepository;
    private final UserRepository userRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final MealService mealService;

    // ========================================================
    // CREATE
    // ========================================================
    @Override
    public NotificationResponse createUserNutrition(UserNutritionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        NutritionPlan plan = nutritionPlanRepository.findById(request.getNutritionPlanId())
                .orElseThrow(() -> new RuntimeException("NutritionPlan not found"));
        System.out.println("Plan found: " + plan.getTitle());
        System.out.println("User found: " + user.getUserName());
        System.out.println("Start Date: " + request.getStartDate());
        System.out.println("End Date: " + request.getEndDate());
        System.out.println("Status: " + request.getStatus());
        UserNutrition un = UserNutrition.builder()
                .user(user)
                .nutritionPlan(plan)
                .startDate(request.getStartDate())

                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : "active")
                .build();

        userNutritionRepository.save(un);

        return new NotificationResponse(true,
                "UserNutrition created successfully",
                convertToResponse(un));
    }

    // ========================================================
    // UPDATE (kiểm tra null)
    // ========================================================
    @Override
    public NotificationResponse updateUserNutrition(Long id, UserNutritionRequest request) {
        UserNutrition un = userNutritionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserNutrition not found"));

        // Update user nếu có
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            un.setUser(user);
        }

        // Update NutritionPlan nếu có
        if (request.getNutritionPlanId() != null) {
            NutritionPlan plan = nutritionPlanRepository.findById(request.getNutritionPlanId())
                    .orElseThrow(() -> new RuntimeException("NutritionPlan not found"));
            un.setNutritionPlan(plan);
        }

        // Update startDate
        if (request.getStartDate() != null) {
            un.setStartDate(request.getStartDate());
        }

        // Update endDate
        if (request.getEndDate() != null) {
            un.setEndDate(request.getEndDate());
        }

        // Update status
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            un.setStatus(request.getStatus());
        }

        userNutritionRepository.save(un);

        return new NotificationResponse(true,
                "UserNutrition updated successfully",
                convertToResponse(un));
    }

    // ========================================================
    // DELETE
    // ========================================================
    @Override
    public NotificationResponse deleteUserNutrition(Long id) {
        if (!userNutritionRepository.existsById(id)) {
            return new NotificationResponse(false, "UserNutrition not found");
        }
        userNutritionRepository.deleteById(id);
        return new NotificationResponse(true, "UserNutrition deleted successfully");
    }

    // ========================================================
    // GET ONE
    // ========================================================
    @Override
    public NotificationResponse getUserNutrition(Long id) {
        UserNutrition un = userNutritionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserNutrition not found"));

        return new NotificationResponse(true,
                "UserNutrition retrieved successfully",
                convertToResponse(un));
    }

    // ========================================================
    // GET ALL
    // ========================================================
    @Override
    public NotificationResponse getAllUserNutrition() {
        List<UserNutritionResponse> list = userNutritionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();

        return new NotificationResponse(true,
                "UserNutrition retrieved successfully",
                list);
    }

    // ========================================================
    // SUPPORT METHOD: convert entity → DTO response
    // ========================================================
    private UserNutritionResponse convertToResponse(UserNutrition un) {

        NutritionPlan plan = un.getNutritionPlan();

        // Lấy tất cả meals theo planId
        List<MealResponse> meals = mealService.getMealByPlanId(plan.getPlanId());

        // Nhóm meals theo dayNumber
        Map<Integer, List<MealResponse>> days = meals.stream()
                .collect(Collectors.groupingBy(MealResponse::getDayNumber));

        // Tính % hoàn thành
        double completion = calculateCompletion(un);

        return UserNutritionResponse.builder()
                .unId(un.getUnId())
                .userId(un.getUser().getId())
                .nutritionPlanId(plan.getPlanId())
                .planTitle(plan.getTitle())
                .targetCalories(plan.getCaloriesPerDay() != null ? plan.getCaloriesPerDay().toString() : "N/A")
                .startDate(un.getStartDate())
                .endDate(un.getEndDate())
                .completion(completion)
                .days(days)
                .status(un.getStatus())
                .build();
    }

    // ========================================================
    // TÍNH COMPLETION
    // ========================================================
    private double calculateCompletion(UserNutrition un) {
        LocalDate start = un.getStartDate();
        LocalDate end = un.getEndDate();
        LocalDate today = LocalDate.now();

        if (start == null || end == null) return 0.0;
        if (end.isBefore(start)) return 0.0;

        // Chưa bắt đầu
        if (today.isBefore(start)) return 0.0;

        // Đã hoàn thành
        if (today.isAfter(end)) return 100.0;

        long totalDays = start.until(end).getDays();
        if (totalDays <= 0) return 100.0; // tránh chia 0

        long passedDays = start.until(today).getDays();

        double percent = ((double) passedDays / totalDays) * 100;

        return Math.max(0, Math.min(percent, 100));
    }
}
