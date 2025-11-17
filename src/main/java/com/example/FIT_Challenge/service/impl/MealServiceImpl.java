package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.Entity.Meal;
import com.example.FIT_Challenge.Entity.NutritionPlan;
import com.example.FIT_Challenge.DTO.MealDTO.MealRequest;
import com.example.FIT_Challenge.DTO.MealDTO.MealResponse;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.MealRepository;
import com.example.FIT_Challenge.repository.NutritionPlanRepository;
import com.example.FIT_Challenge.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final NutritionPlanRepository nutritionPlanRepository;

    private MealResponse toResponse(Meal meal) {
        MealResponse dto = new MealResponse();
        dto.setMealId(meal.getMealId());
        dto.setNutritionPlanId(meal.getNutritionPlan().getPlanId());
        dto.setNutritionPlanTitle(meal.getNutritionPlan().getTitle());
        dto.setMealType(meal.getMealType());
        dto.setName(meal.getName());
        dto.setDescription(meal.getDescription());
        dto.setCaloriesEstimate(meal.getCaloriesEstimate());
        return dto;
    }

    @Override
    public NotificationResponse createMeal(MealRequest request) {
        return nutritionPlanRepository.findById(request.getNutritionPlanId())
                .map(plan -> {
                    Meal meal = new Meal();
                    meal.setNutritionPlan(plan);
                    meal.setMealType(request.getMealType());
                    meal.setName(request.getName());
                    meal.setDescription(request.getDescription());
                    meal.setCaloriesEstimate(request.getCaloriesEstimate());
                    Meal saved = mealRepository.save(meal);
                    return new NotificationResponse(true, "Meal created successfully", toResponse(saved));
                })
                .orElseGet(() -> new NotificationResponse(false, "Nutrition plan not found with ID: " + request.getNutritionPlanId()));
    }

    @Override
    public NotificationResponse updateMeal(Long id, MealRequest request) {
        return mealRepository.findById(id)
                .map(meal -> {
                    if (request.getNutritionPlanId() != null) {
                        nutritionPlanRepository.findById(request.getNutritionPlanId())
                                .ifPresent(meal::setNutritionPlan);
                    }
                    if (request.getMealType() != null) meal.setMealType(request.getMealType());
                    if (request.getName() != null) meal.setName(request.getName());
                    if (request.getDescription() != null) meal.setDescription(request.getDescription());
                    if (request.getCaloriesEstimate() != null) meal.setCaloriesEstimate(request.getCaloriesEstimate());

                    Meal updated = mealRepository.save(meal);
                    return new NotificationResponse(true, "Meal updated successfully", toResponse(updated));
                })
                .orElseGet(() -> new NotificationResponse(false, "Meal not found with ID: " + id));
    }

    @Override
    public NotificationResponse deleteMeal(Long id) {
        if (!mealRepository.existsById(id)) {
            return new NotificationResponse(false, "Meal not found with ID: " + id);
        }
        mealRepository.deleteById(id);
        return new NotificationResponse(true, "Meal deleted successfully");
    }

    @Override
    public NotificationResponse getMealById(Long id) {
        return mealRepository.findById(id)
                .map(meal -> new NotificationResponse(true, "Success", toResponse(meal)))
                .orElseGet(() -> new NotificationResponse(false, "Meal not found with ID: " + id));
    }

    @Override
    public NotificationResponse getAllMeals() {
        List<MealResponse> list = mealRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new NotificationResponse(true, "All meals retrieved", list);
    }

    @Override
    public NotificationResponse getMealsByPlan(Long planId) {
        List<Meal> meals = mealRepository.findByNutritionPlanPlanId(planId);
        if (meals.isEmpty()) {
            return new NotificationResponse(false, "No meals found for plan ID: " + planId);
        }
        List<MealResponse> list = meals.stream().map(this::toResponse).collect(Collectors.toList());
        return new NotificationResponse(true, "Meals retrieved for plan ID: " + planId, list);
    }
}
