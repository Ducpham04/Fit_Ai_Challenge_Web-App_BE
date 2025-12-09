package com.example.fitchallenge.controller.Admin;


import com.example.fitchallenge.DTO.TrainingPlanDetailDTO.TrainingPlanDetailRequest;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.TrainingPlanDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/training-plan-details")
@RequiredArgsConstructor
public class TrainingPlanDetailController {

    private final TrainingPlanDetailService trainingPlanDetailService;

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody TrainingPlanDetailRequest dto) {
        return ResponseEntity.ok(trainingPlanDetailService.createDetail(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> update(@PathVariable Long id, @RequestBody TrainingPlanDetailRequest dto) {
        return ResponseEntity.ok(trainingPlanDetailService.updateDetail(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanDetailService.deleteDetail(id));
    }

    @GetMapping
    public ResponseEntity<NotificationResponse> getAll() {
        return ResponseEntity.ok(trainingPlanDetailService.getAllDetails());
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<NotificationResponse> getByPlan(@PathVariable Long planId) {
        return ResponseEntity.ok(trainingPlanDetailService.getDetailsByPlanId(planId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanDetailService.getDetailById(id));
    }
}
