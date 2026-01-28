package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.dto.mealdto.MealRequest;
import com.example.fitchallenge.dto.mealdto.MealResponse;
import com.example.fitchallenge.dto.mealfooddto.MealFoodRequest;
import com.example.fitchallenge.dto.mealfooddto.MealFoodResponse;
import com.example.fitchallenge.entity.Food;
import com.example.fitchallenge.entity.Meal;
import com.example.fitchallenge.entity.MealFood;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.FoodRepository;
import com.example.fitchallenge.repository.MealFoodRepository;
import com.example.fitchallenge.repository.MealRepository;
import com.example.fitchallenge.repository.NutritionPlanRepository;
import com.example.fitchallenge.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    private final MealFoodRepository mealFoodRepository;
    private final FoodRepository foodRepository;

    // ===================== Mapping Entity -> DTO =====================
    private MealFoodResponse mapMealFoodToResponse(MealFood mf) {
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

    private MealResponse mapMealToResponse(Meal meal) {
        MealResponse dto = new MealResponse();
        dto.setMealId(meal.getMealId());
        dto.setNutritionPlanId(meal.getNutritionPlan().getPlanId());
        dto.setMealType(meal.getMealType());
        dto.setDayNumber(meal.getDay());
        dto.setName(meal.getName());
        dto.setDescription(meal.getDescription());
        dto.setCaloriesEstimate(meal.getCaloriesEstimate());

        List<MealFoodResponse> foods = mealFoodRepository.findByMealMealId(meal.getMealId())
                .stream()
                .map(this::mapMealFoodToResponse)
                .collect(Collectors.toList());
        dto.setFoods(foods);

        return dto;
    }

    // ===================== CRUD =====================
    @Override
    public NotificationResponse createMeal(MealRequest request) {
        return nutritionPlanRepository.findById(request.getNutritionPlanId())
                .map(plan -> {
                    Meal meal = new Meal();
                    meal.setNutritionPlan(plan);
                    meal.setMealType(request.getMealType());
                    meal.setName(request.getName());
                    meal.setDescription(request.getDescription());

                    // Tạo Meal trước để có ID
                    Meal savedMeal = mealRepository.save(meal);

                    // Xử lý MealFood nếu có
                    List<MealFoodRequest> foodRequests = request.getFoods();
                    List<MealFood> mealFoods = new ArrayList<>();
                    int totalCalories = 0;

                    if (foodRequests != null) {
                        for (MealFoodRequest mfReq : foodRequests) {
                            Food food = foodRepository.findById(mfReq.getFoodId())
                                    .orElseThrow(() -> new RuntimeException("Food not found with ID: " + mfReq.getFoodId()));

                            MealFood mf = new MealFood();
                            mf.setMeal(savedMeal);
                            mf.setFood(food);
                            mf.setQuantityG(mfReq.getQuantityG());

                            totalCalories += food.getCaloriesPer100g() * mfReq.getQuantityG() / 100;
                            mealFoods.add(mf);
                        }
                        mealFoodRepository.saveAll(mealFoods);
                    }

                    savedMeal.setCaloriesEstimate(totalCalories);
                    mealRepository.save(savedMeal);

                    return new NotificationResponse(true, "Meal created successfully", mapMealToResponse(savedMeal));
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

                    // Xử lý MealFood: xóa cũ, thêm mới
                    List<MealFoodRequest> foodRequests = request.getFoods();
                    if (foodRequests != null) {
                        List<MealFood> existing = mealFoodRepository.findByMealMealId(meal.getMealId());
                        mealFoodRepository.deleteAll(existing);

                        List<MealFood> mealFoods = new ArrayList<>();
                        int totalCalories = 0;

                        for (MealFoodRequest mfReq : foodRequests) {
                            Food food = foodRepository.findById(mfReq.getFoodId())
                                    .orElseThrow(() -> new RuntimeException("Food not found with ID: " + mfReq.getFoodId()));

                            MealFood mf = new MealFood();
                            mf.setMeal(meal);
                            mf.setFood(food);
                            mf.setQuantityG(mfReq.getQuantityG());

                            totalCalories += food.getCaloriesPer100g() * mfReq.getQuantityG() / 100;
                            mealFoods.add(mf);
                        }
                        mealFoodRepository.saveAll(mealFoods);
                        meal.setCaloriesEstimate(totalCalories);
                    }

                    Meal updated = mealRepository.save(meal);
                    return new NotificationResponse(true, "Meal updated successfully", mapMealToResponse(updated));
                })
                .orElseGet(() -> new NotificationResponse(false, "Meal not found with ID: " + id));
    }

    @Override
    public NotificationResponse deleteMeal(Long id) {
        if (!mealRepository.existsById(id)) {
            return new NotificationResponse(false, "Meal not found with ID: " + id);
        }
        List<MealFood> mealFoods = mealFoodRepository.findByMealMealId(id);
        mealFoodRepository.deleteAll(mealFoods);
        mealRepository.deleteById(id);
        return new NotificationResponse(true, "Meal deleted successfully");
    }

    @Override
    public NotificationResponse getMealById(Long id) {
        return mealRepository.findById(id)
                .map(meal -> new NotificationResponse(true, "Success", mapMealToResponse(meal)))
                .orElseGet(() -> new NotificationResponse(false, "Meal not found with ID: " + id));
    }

    @Override
    public NotificationResponse getAllMeals() {
        List<MealResponse> list = mealRepository.findAll()
                .stream()
                .map(this::mapMealToResponse)
                .collect(Collectors.toList());
        return new NotificationResponse(true, "All meals retrieved", list);
    }

    @Override
    public NotificationResponse getMealsByPlan(Long planId) {
        List<Meal> meals = mealRepository.findByNutritionPlanPlanId(planId);
        if (meals.isEmpty()) {
            return new NotificationResponse(false, "No meals found for plan ID: " + planId);
        }
        List<MealResponse> list = meals.stream().map(this::mapMealToResponse).collect(Collectors.toList());
        return new NotificationResponse(true, "Meals retrieved for plan ID: " + planId, list);
    }

    @Override
    public List<MealResponse> getMealByPlanId(Long planId) {
        List<Meal> meals = mealRepository.findByNutritionPlanPlanId(planId);
        if (!meals.isEmpty()) {
            return meals.stream().map(this::mapMealToResponse).collect(Collectors.toList());
        }
       return null ;
    }
}
