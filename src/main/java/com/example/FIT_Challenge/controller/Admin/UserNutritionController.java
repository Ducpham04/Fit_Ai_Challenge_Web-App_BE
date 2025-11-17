package com.example.FIT_Challenge.controller.Admin;

import com.example.FIT_Challenge.DTO.UserNutritionDTO.UserNutritionRequest;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.UserNutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-nutrition")
@RequiredArgsConstructor
public class UserNutritionController {

    private final UserNutritionService userNutritionService;

    @PostMapping
    public NotificationResponse create(@RequestBody UserNutritionRequest request){
        return userNutritionService.createUserNutrition(request);
    }

    @PutMapping("/{id}")
    public NotificationResponse update(@PathVariable Long id, @RequestBody UserNutritionRequest request){
        return userNutritionService.updateUserNutrition(id, request);
    }

    @DeleteMapping("/{id}")
    public NotificationResponse delete(@PathVariable Long id){
        return userNutritionService.deleteUserNutrition(id);
    }
    @GetMapping
    public NotificationResponse getAllUserNutrition(){
        return userNutritionService.getAllUserNutrition();
    }
}
