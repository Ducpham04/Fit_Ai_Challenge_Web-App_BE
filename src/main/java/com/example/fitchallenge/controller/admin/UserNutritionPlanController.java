package com.example.fitchallenge.controller.admin;

import com.example.fitchallenge.dto.usernutritionfto.UserNutritionRequest;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserNutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-nutrition")
@RequiredArgsConstructor
public class UserNutritionPlanController {

    private final UserNutritionService userNutritionService;

    // ========================================================
    // CREATE USER NUTRITION
    // ========================================================
    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody UserNutritionRequest request) {
        NotificationResponse response = userNutritionService.createUserNutrition(request);
        return ResponseEntity.status(201).body(response);
    }

    // ========================================================
    // UPDATE USER NUTRITION
    // ========================================================
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> update(
            @PathVariable Long id,
            @RequestBody UserNutritionRequest request
    ) {
        NotificationResponse response = userNutritionService.updateUserNutrition(id, request);
        return ResponseEntity.ok(response);
    }

    // ========================================================
    // DELETE USER NUTRITION
    // ========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> delete(@PathVariable Long id) {
        NotificationResponse response = userNutritionService.deleteUserNutrition(id);

        if (!response.isSuccess()) {
            return ResponseEntity.status(404).body(response);
        }

        return ResponseEntity.ok(response);
    }

    // ========================================================
    // GET 1 USER NUTRITION
    // ========================================================
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getOne(@PathVariable Long id) {
        NotificationResponse response = userNutritionService.getUserNutrition(id);

        if (!response.isSuccess()) {
            return ResponseEntity.status(404).body(response);
        }

        return ResponseEntity.ok(response);
    }

    // ========================================================
    // GET ALL USER NUTRITION
    // ========================================================
    @GetMapping
    public ResponseEntity<NotificationResponse> getAll() {
        NotificationResponse response = userNutritionService.getAllUserNutrition();
        return ResponseEntity.ok(response);
    }
}
