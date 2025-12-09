package com.example.fitchallenge.controller.User;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.DailyTrainingLogService;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/daily-training-logs")
@RequiredArgsConstructor
public class DailyTrainingLogController {

    private final DailyTrainingLogService dailyTrainingLogService;
    private final UserService userService;

    /**
     * GET /api/user/daily-training-logs/plan/{trainingPlanId}
     * Lấy tất cả daily training logs của user hiện tại trong một training plan
     * Kết hợp với challenge information từ template
     */
    @GetMapping("/plan/{trainingPlanId}")
    public ResponseEntity<NotificationResponse> getDailyTrainingLogsByPlan(
            @PathVariable Long trainingPlanId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }
        
        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            NotificationResponse response = dailyTrainingLogService
                    .getDailyTrainingLogsByUserAndPlan(userId, trainingPlanId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }

    /**
     * GET /api/user/daily-training-logs/plan/{trainingPlanId}/day/{dayNumber}
     * Lấy daily training logs của user hiện tại trong một training plan theo day number
     */
    @GetMapping("/plan/{trainingPlanId}/day/{dayNumber}")
    public ResponseEntity<NotificationResponse> getDailyTrainingLogsByPlanAndDay(
            @PathVariable Long trainingPlanId,
            @PathVariable Integer dayNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }
        
        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            NotificationResponse response = dailyTrainingLogService
                    .getDailyTrainingLogsByUserAndPlanAndDay(userId, trainingPlanId, dayNumber);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }

    /**
     * POST /api/user/daily-training-logs
     * Tạo hoặc cập nhật daily training log
     * Nhận analysisData từ request body (JSON)
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> createOrUpdateDailyTrainingLog(
            @RequestParam Long trainingPlanId,
            @RequestParam Integer dayNumber,
            @RequestParam Long challengeId,
            @RequestParam String status,
            @RequestBody(required = false) java.util.Map<String, Object> analysisData,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        System.out.println("🔵 [DailyTrainingLogController] POST /daily-training-logs called");
        System.out.println("   - trainingPlanId: " + trainingPlanId);
        System.out.println("   - dayNumber: " + dayNumber);
        System.out.println("   - challengeId: " + challengeId);
        System.out.println("   - status: " + status);
        System.out.println("   - analysisData: " + (analysisData != null ? analysisData.toString() : "null"));
        System.out.println("   - userDetails: " + (userDetails != null ? userDetails.getUsername() : "null"));
        
        if (userDetails == null) {
            System.err.println("❌ [DailyTrainingLogController] Unauthorized - userDetails is null");
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }
        
        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            System.out.println("✅ [DailyTrainingLogController] User ID: " + userId);
            
            // Extract analysis data from request body
            Integer repsCompleted = null;
            Integer setsCompleted = null;
            Integer score = null;
            Double confidence = null;
            Integer actualDurationMinutes = null;
            
            if (analysisData != null) {
                if (analysisData.containsKey("repsCompleted")) {
                    repsCompleted = analysisData.get("repsCompleted") instanceof Integer 
                            ? (Integer) analysisData.get("repsCompleted")
                            : ((Number) analysisData.get("repsCompleted")).intValue();
                }
                if (analysisData.containsKey("setsCompleted")) {
                    setsCompleted = analysisData.get("setsCompleted") instanceof Integer 
                            ? (Integer) analysisData.get("setsCompleted")
                            : ((Number) analysisData.get("setsCompleted")).intValue();
                }
                if (analysisData.containsKey("score")) {
                    score = analysisData.get("score") instanceof Integer 
                            ? (Integer) analysisData.get("score")
                            : ((Number) analysisData.get("score")).intValue();
                }
                if (analysisData.containsKey("confidence")) {
                    confidence = analysisData.get("confidence") instanceof Double 
                            ? (Double) analysisData.get("confidence")
                            : ((Number) analysisData.get("confidence")).doubleValue();
                }
                if (analysisData.containsKey("actualDurationMinutes")) {
                    actualDurationMinutes = analysisData.get("actualDurationMinutes") instanceof Integer 
                            ? (Integer) analysisData.get("actualDurationMinutes")
                            : ((Number) analysisData.get("actualDurationMinutes")).intValue();
                }
            }
            
            System.out.println("📤 [DailyTrainingLogController] Calling service with:");
            System.out.println("   - userId: " + userId);
            System.out.println("   - repsCompleted: " + repsCompleted);
            System.out.println("   - setsCompleted: " + setsCompleted);
            System.out.println("   - score: " + score);
            System.out.println("   - confidence: " + confidence);
            System.out.println("   - actualDurationMinutes: " + actualDurationMinutes);
            
            NotificationResponse response = dailyTrainingLogService
                    .createOrUpdateDailyTrainingLog(
                            userId, 
                            trainingPlanId, 
                            dayNumber, 
                            challengeId, 
                            status,
                            repsCompleted,
                            setsCompleted,
                            score,
                            confidence,
                            actualDurationMinutes);
            
            System.out.println("✅ [DailyTrainingLogController] Service response: " + 
                    (response.isSuccess() ? "SUCCESS" : "FAILED") + 
                    " - " + response.getMessage());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.err.println("❌ [DailyTrainingLogController] IllegalArgumentException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                new NotificationResponse(false, "Invalid request: " + e.getMessage())
            );
        } catch (RuntimeException e) {
            System.err.println("❌ [DailyTrainingLogController] RuntimeException: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(400).body(
                new NotificationResponse(false, e.getMessage())
            );
        } catch (Exception e) {
            System.err.println("❌ [DailyTrainingLogController] Exception occurred:");
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Internal server error: " + e.getMessage())
            );
        }
    }
}

