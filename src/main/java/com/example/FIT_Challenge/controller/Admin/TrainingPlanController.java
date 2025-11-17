package com.example.FIT_Challenge.controller.Admin;



import com.example.FIT_Challenge.DTO.TraningPlanDTO.TrainingPlanRequestDTO;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.TrainingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/training-plans")
@RequiredArgsConstructor
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @GetMapping
    public ResponseEntity<NotificationResponse> getAllTrainingPlans() {
        return ResponseEntity.ok(trainingPlanService.getAllTrainingPlans());
    }

    @GetMapping("/{tpId}")
    public ResponseEntity<NotificationResponse> getTrainingPlanById(@PathVariable Long tpId) {
        return ResponseEntity.ok(trainingPlanService.getTrainingPlanById(tpId));
    }

    @GetMapping("/goal/{goalId}")
    public ResponseEntity<NotificationResponse> getTrainingPlansByGoal(@PathVariable Long goalId) {
        return ResponseEntity.ok(trainingPlanService.getTrainingPlansByGoalId(goalId));
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createTrainingPlan(@RequestBody TrainingPlanRequestDTO dto) {
        return ResponseEntity.ok(trainingPlanService.createTrainingPlan(dto));
    }

    @PutMapping("/{tpId}")
    public ResponseEntity<NotificationResponse> updateTrainingPlan(@PathVariable Long tpId,
                                                                   @RequestBody TrainingPlanRequestDTO dto) {
        return ResponseEntity.ok(trainingPlanService.updateTrainingPlan(tpId, dto));
    }

    @DeleteMapping("/{tpId}")
    public ResponseEntity<NotificationResponse> deleteTrainingPlan(@PathVariable Long tpId) {
        return ResponseEntity.ok(trainingPlanService.deleteTrainingPlan(tpId));
    }
}
