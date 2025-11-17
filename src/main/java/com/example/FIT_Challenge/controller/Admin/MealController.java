package com.example.FIT_Challenge.controller.Admin;

import com.example.FIT_Challenge.DTO.MealDTO.MealRequest;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    // 🔹 Tạo meal mới
    @PostMapping
    public NotificationResponse createMeal(@RequestBody MealRequest request) {
        return mealService.createMeal(request);
    }

    // 🔹 Cập nhật meal
    @PutMapping("/{id}")
    public NotificationResponse updateMeal(@PathVariable Long id, @RequestBody MealRequest request) {
        return mealService.updateMeal(id, request);
    }

    // 🔹 Xóa meal
    @DeleteMapping("/{id}")
    public NotificationResponse deleteMeal(@PathVariable Long id) {
        return mealService.deleteMeal(id);
    }

    // 🔹 Lấy meal theo ID
    @GetMapping("/{id}")
    public NotificationResponse getMealById(@PathVariable Long id) {
        return mealService.getMealById(id);
    }

    // 🔹 Lấy tất cả meal
    @GetMapping
    public NotificationResponse getAllMeals() {
        return mealService.getAllMeals();
    }

    // 🔹 Lấy meal theo kế hoạch dinh dưỡng
    @GetMapping("/plan/{planId}")
    public NotificationResponse getMealsByPlan(@PathVariable Long planId) {
        return mealService.getMealsByPlan(planId);
    }
}
