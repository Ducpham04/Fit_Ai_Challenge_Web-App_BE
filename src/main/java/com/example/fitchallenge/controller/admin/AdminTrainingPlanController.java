package com.example.fitchallenge.controller.admin;



import com.example.fitchallenge.dto.traningplandto.TrainingPlanRequest;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.TrainingPlanService;
import com.example.fitchallenge.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/training-plans")
@RequiredArgsConstructor
public class AdminTrainingPlanController {

    private final TrainingPlanService trainingPlanService;
    private final UserTrainingService userTrainingService;

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
    public ResponseEntity<NotificationResponse> createTrainingPlan(@RequestBody TrainingPlanRequest dto) {
        return ResponseEntity.ok(trainingPlanService.createTrainingPlan(dto));
    }

    @PutMapping("/{tpId}")
    public ResponseEntity<NotificationResponse> updateTrainingPlan(@PathVariable Long tpId,
                                                                   @RequestBody TrainingPlanRequest dto) {
        return ResponseEntity.ok(trainingPlanService.updateTrainingPlan(tpId, dto));
    }

    @DeleteMapping("/{tpId}")
    public ResponseEntity<NotificationResponse> deleteTrainingPlan(@PathVariable Long tpId) {
        return ResponseEntity.ok(trainingPlanService.deleteTrainingPlan(tpId));
    }

    /**
     * POST /api/admin/training-plans/{id}/duplicate
     * Sao chép plan để tạo phiên bản mới
     */
    @PostMapping("/{tpId}/duplicate")
    public ResponseEntity<NotificationResponse> duplicateTrainingPlan(@PathVariable Long tpId) {
        return ResponseEntity.ok(trainingPlanService.duplicateTrainingPlan(tpId));
    }

    /**
     * PUT /api/admin/training-plans/{id}/publish
     * Publish/Unpublish plan
     */
    @PutMapping("/{tpId}/publish")
    public ResponseEntity<NotificationResponse> publishTrainingPlan(
            @PathVariable Long tpId,
            @RequestParam(defaultValue = "true") boolean publish) {
        return ResponseEntity.ok(trainingPlanService.publishTrainingPlan(tpId, publish));
    }

    /**
     * GET /api/admin/training-plans/{tpId}/users
     * Lấy danh sách users đang theo training plan
     */
    @GetMapping("/{tpId}/users")
    public ResponseEntity<NotificationResponse> getUsersFollowingTrainingPlan(@PathVariable Long tpId) {
        return ResponseEntity.ok(userTrainingService.getUsersFollowingTrainingPlan(tpId));
    }
}
