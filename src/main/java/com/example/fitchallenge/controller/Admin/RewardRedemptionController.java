package com.example.fitchallenge.controller.Admin;

import com.example.fitchallenge.DTO.RewardRedemptionDTO.RewardRedemptionRequest;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.RewardRedemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reward-redemptions")
@RequiredArgsConstructor
public class RewardRedemptionController {

    private final RewardRedemptionService rewardRedemptionService;

    // ➕ Tạo yêu cầu đổi quà
    @PostMapping
    public ResponseEntity<NotificationResponse> redeemReward(@RequestBody RewardRedemptionRequest request) {
        NotificationResponse response = rewardRedemptionService.redeemReward(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    // 📋 Lấy tất cả các yêu cầu đổi quà
    @GetMapping
    public ResponseEntity<NotificationResponse> getAllRedemptions() {
        NotificationResponse response = rewardRedemptionService.getAllRedemptions();
        return ResponseEntity.ok(response);
    }

    // ✏️ Cập nhật trạng thái
    @PutMapping("/{redemptionId}/status")
    public ResponseEntity<NotificationResponse> updateStatus(
            @PathVariable Long redemptionId,
            @RequestParam String status) {
        NotificationResponse response = rewardRedemptionService.updateStatus(redemptionId, status);
        return ResponseEntity.ok(response);
    }
}
