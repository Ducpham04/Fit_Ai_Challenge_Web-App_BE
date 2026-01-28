package com.example.fitchallenge.controller;

import com.example.fitchallenge.dto.usertrainingdto.UserRequestDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/training")
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
