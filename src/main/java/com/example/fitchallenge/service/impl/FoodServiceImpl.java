package com.example.fitchallenge.service.impl;



import com.example.fitchallenge.DTO.FoodDTO.FoodRequest;
import com.example.fitchallenge.DTO.FoodDTO.FoodResponse;
import com.example.fitchallenge.Entity.Food;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.FoodRepository;
import com.example.fitchallenge.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    private FoodResponse toResponse(Food food){
        return FoodResponse.builder()
                .id(food.getFoodId())
                .name(food.getName())
                .calories(food.getCaloriesPer100g())
                .protein(food.getProteinPer100g())
                .carbs(food.getCarbsPer100g())
                .fat(food.getFatPer100g())
                .notes(food.getNotes())
                .build();
    }

    @Override
    public NotificationResponse createFood(FoodRequest request) {
        Food food = Food.builder()
                .name(request.getName())
                .caloriesPer100g(request.getCalories())
                .proteinPer100g(request.getProtein())
                .carbsPer100g(request.getCarbs())
                .fatPer100g(request.getFat())
                .notes(request.getNotes())
                .build();
        foodRepository.save(food);
        return new NotificationResponse(true, "Food created successfully", toResponse(food));
    }

    @Override
    public NotificationResponse updateFood(Long id, FoodRequest request) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with ID: " + id));
        food.setName(request.getName());
        food.setCaloriesPer100g(request.getCalories());
        food.setProteinPer100g(request.getProtein());
        food.setCarbsPer100g(request.getCarbs());
        food.setFatPer100g(request.getFat());
        food.setNotes(request.getNotes());
        foodRepository.save(food);
        return new NotificationResponse(true, "Food updated successfully", toResponse(food));
    }

    @Override
    public NotificationResponse deleteFood(Long id) {
        if(!foodRepository.existsById(id)){
            return new NotificationResponse(false, "Food not found");
        }
        foodRepository.deleteById(id);
        return new NotificationResponse(true, "Food deleted successfully");
    }

    @Override
    public NotificationResponse getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with ID: " + id));
        return new NotificationResponse(true, "Success", toResponse(food));
    }

    @Override
    public NotificationResponse getAllFoods() {
        List<FoodResponse> list = foodRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new NotificationResponse(true, "All foods", list);
    }
}

