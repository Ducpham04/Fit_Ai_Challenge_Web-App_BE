package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.MealFoodDTO.MealFoodRequest;
import com.example.FIT_Challenge.DTO.MealFoodDTO.MealFoodResponse;
import com.example.FIT_Challenge.Entity.Food;
import com.example.FIT_Challenge.Entity.Meal;
import com.example.FIT_Challenge.Entity.MealFood;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.FoodRepository;
import com.example.FIT_Challenge.repository.MealRepository;
import com.example.FIT_Challenge.repository.MealFoodRepository;
import com.example.FIT_Challenge.service.MealFoodService;
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

        MealFoodResponse dto = new MealFoodResponse(
                mf.getMfId(),
                meal.getMealId(),
                food.getFoodId(),
                mf.getQuantityG()

        );

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

        MealFoodResponse dto = new MealFoodResponse(
                mf.getMfId(),
                meal.getMealId(),
                food.getFoodId(),
                mf.getQuantityG()

        );

        return new NotificationResponse(true, "MealFood updated successfully", dto);
    }

    @Override
    public NotificationResponse deleteMealFood(Long id) {
        if(!mealFoodRepository.existsById(id)){
            return new NotificationResponse(false, "MealFood not found");
        }
        mealFoodRepository.deleteById(id);
        return new NotificationResponse(true, "MealFood deleted successfully");
    }
}
