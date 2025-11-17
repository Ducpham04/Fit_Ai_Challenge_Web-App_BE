package com.example.FIT_Challenge.service;



import com.example.FIT_Challenge.DTO.FoodDTO.FoodRequest;
import com.example.FIT_Challenge.DTO.FoodDTO.FoodResponse;
import com.example.FIT_Challenge.config.NotificationResponse;

import java.util.List;

public interface FoodService {
    NotificationResponse createFood(FoodRequest request);
    NotificationResponse updateFood(Long id, FoodRequest request);
    NotificationResponse deleteFood(Long id);
    NotificationResponse getFoodById(Long id);
    NotificationResponse getAllFoods();
}

