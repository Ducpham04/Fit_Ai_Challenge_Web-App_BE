package com.example.FIT_Challenge.service.impl;



import com.example.FIT_Challenge.DTO.FoodDTO.FoodRequest;
import com.example.FIT_Challenge.DTO.FoodDTO.FoodResponse;
import com.example.FIT_Challenge.Entity.Food;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.FoodRepository;
import com.example.FIT_Challenge.service.FoodService;
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
                .foodId(food.getFoodId())
                .name(food.getName())
                .caloriesPer100g(food.getCaloriesPer100g())
                .proteinPer100g(food.getProteinPer100g())
                .carbsPer100g(food.getCarbsPer100g())
                .fatPer100g(food.getFatPer100g())
                .notes(food.getNotes())
                .build();
    }

    @Override
    public NotificationResponse createFood(FoodRequest request) {
        Food food = Food.builder()
                .name(request.getName())
                .caloriesPer100g(request.getCaloriesPer100g())
                .proteinPer100g(request.getProteinPer100g())
                .carbsPer100g(request.getCarbsPer100g())
                .fatPer100g(request.getFatPer100g())
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
        food.setCaloriesPer100g(request.getCaloriesPer100g());
        food.setProteinPer100g(request.getProteinPer100g());
        food.setCarbsPer100g(request.getCarbsPer100g());
        food.setFatPer100g(request.getFatPer100g());
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

