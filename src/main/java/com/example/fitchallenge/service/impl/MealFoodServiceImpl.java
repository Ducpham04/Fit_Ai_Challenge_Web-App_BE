package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.MealFoodDTO.MealFoodRequest;
import com.example.fitchallenge.DTO.MealFoodDTO.MealFoodResponse;
import com.example.fitchallenge.Entity.Food;
import com.example.fitchallenge.Entity.Meal;
import com.example.fitchallenge.Entity.MealFood;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.FoodRepository;
import com.example.fitchallenge.repository.MealRepository;
import com.example.fitchallenge.repository.MealFoodRepository;
import com.example.fitchallenge.service.MealFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MealFoodServiceImpl implements MealFoodService {

    private final MealFoodRepository mealFoodRepository;
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    @Override
    public NotificationResponse createMealFood(MealFoodRequest request) {

        Meal meal = mealRepository.findById(request.getMealId())
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food not found"));

        MealFood mf = new MealFood();
        mf.setMeal(meal);
        mf.setFood(food);
        mf.setQuantityG(request.getQuantityG());

        mealFoodRepository.save(mf);

        MealFoodResponse dto = mapToResponse(mf);

        return new NotificationResponse(true, "Created successfully", dto);
    }

    @Override
    public NotificationResponse updateMealFood(Long id, MealFoodRequest request) {
        MealFood mf = mealFoodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MealFood not found"));

        Meal meal = mealRepository.findById(request.getMealId())
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food not found"));

        mf.setMeal(meal);
        mf.setFood(food);
        mf.setQuantityG(request.getQuantityG());

        mealFoodRepository.save(mf);

        MealFoodResponse dto = mapToResponse(mf);

        return new NotificationResponse(true, "MealFood updated successfully", dto);
    }

    @Override
    public NotificationResponse deleteMealFood(Long id) {
        if (!mealFoodRepository.existsById(id)) {
            return new NotificationResponse(false, "MealFood not found");
        }

        mealFoodRepository.deleteById(id);
        return new NotificationResponse(true, "MealFood deleted successfully");
    }

    // Phương thức tiện ích để map Entity -> DTO
    private MealFoodResponse mapToResponse(MealFood mf) {
        Food food = mf.getFood();
        int quantity = mf.getQuantityG();

        return MealFoodResponse.builder()
                .mfId(mf.getMfId())
                .foodId(food.getFoodId())
                .foodName(food.getName())
                .quantityG(quantity)
                .totalCalories(food.getCaloriesPer100g() * quantity / 100)
                .totalProtein(food.getProteinPer100g() * quantity / 100.0)
                .totalCarbs(food.getCarbsPer100g() * quantity / 100.0)
                .totalFat(food.getFatPer100g() * quantity / 100.0)
                .build();
    }
}
