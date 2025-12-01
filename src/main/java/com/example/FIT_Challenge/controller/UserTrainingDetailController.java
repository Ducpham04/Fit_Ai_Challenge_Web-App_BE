package com.example.FIT_Challenge.controller;

import com.example.FIT_Challenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/training-details")
@RequiredArgsConstructor
public class UserTrainingDetailController {
    private final UserTrainingService userTrainingService;
    @GetMapping("/{userId}")
    public NotificationResponse getUserTrainingDetails(@PathVariable Long userId) {
        System.out.println("Fetching training details for user ID: " + userId);
        return userTrainingService.getUserTrainingDetails(userId);
    }

    @PostMapping
    public NotificationResponse createUserTraining(@RequestBody UserRequestDTO res) {
        System.out.println("Creating training for user ID: " + res.getUserID());
        return userTrainingService.createUserTraining(res);
    }
}
