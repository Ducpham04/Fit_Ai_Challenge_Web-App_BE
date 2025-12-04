package com.example.fitchallenge.service;



import com.example.fitchallenge.DTO.FoodDTO.FoodRequest;
import com.example.fitchallenge.config.NotificationResponse;

public interface FoodService {
    NotificationResponse createFood(FoodRequest request);
    NotificationResponse updateFood(Long id, FoodRequest request);
    NotificationResponse deleteFood(Long id);
    NotificationResponse getFoodById(Long id);
    NotificationResponse getAllFoods();
}

