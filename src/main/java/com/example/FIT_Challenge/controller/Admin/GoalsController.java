package com.example.FIT_Challenge.controller.Admin;

import com.example.FIT_Challenge.DTO.goalsDTO.goalsDTOpayload;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.GoalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/goals")
public class GoalsController {

    private final GoalService goalService;

    // Constructor injection là chuẩn nhất
    public GoalsController(GoalService goalService) {
        this.goalService = goalService;
    }

    // ✅ CREATE
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<NotificationResponse> createGoal(
            @RequestPart("data") goalsDTOpayload goal,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        System.out.println("Received goal data: " + goal);
        NotificationResponse response = goalService.createGoal(goal, image);
        System.out.println("Service response: " + response);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<NotificationResponse> getAllGoals() {
        List<goalsDTOpayload> goals = goalService.getGoals();
        if (goals.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new NotificationResponse(false, "No goals found"));
        }
        return ResponseEntity.ok(
                new NotificationResponse(true, "Goals retrieved successfully", goals)
        );
    }

    // ✅ UPDATE (PUT)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NotificationResponse> updateGoal(
            @PathVariable Long id,
            @RequestPart("data") String data,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            goalsDTOpayload dto = mapper.readValue(data, goalsDTOpayload.class);

            NotificationResponse response = goalService.update(id, dto, image);

            if (!response.isSuccess()) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new NotificationResponse(false, "Invalid request: " + e.getMessage()));
        }
    }


    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> deleteGoal(@PathVariable Long id) {
        NotificationResponse response = goalService.delete(id);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
