package com.example.FIT_Challenge.controller.Admin;

import com.example.FIT_Challenge.DTO.MealFoodDTO.MealFoodRequest;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.MealFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meal-foods")
@RequiredArgsConstructor
public class MealFoodController {

    private final MealFoodService mealFoodService;

    @PostMapping
    public NotificationResponse create(@RequestBody MealFoodRequest request){
        return mealFoodService.createMealFood(request);
    }

    @PutMapping("/{id}")
    public NotificationResponse update(@PathVariable Long id, @RequestBody MealFoodRequest request){
        return mealFoodService.updateMealFood(id, request);
    }

    @DeleteMapping("/{id}")
    public NotificationResponse delete(@PathVariable Long id){
        return mealFoodService.deleteMealFood(id);
    }
}
