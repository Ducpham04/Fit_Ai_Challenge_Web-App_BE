package com.example.fitchallenge.controller.user;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.PersonalizationService;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class PersonalizedTrainingPlanController {

    private final PersonalizationService personalizationService;
    private final UserService userService;

    /**
     * GET /api/user/training/{utId}/day/{dayNumber}
     * Lấy danh sách bài tập đã được cá nhân hóa cho một ngày cụ thể
     * (Giữ lại để backward compatibility)
     */
    @GetMapping("/training/{utId}/day/{dayNumber}")
    public ResponseEntity<NotificationResponse> getPersonalizedDayDetails(
            @PathVariable Long utId,
            @PathVariable Integer dayNumber) {
        try {
            // Verify user owns this training plan
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                return ResponseEntity.ok(new NotificationResponse(false, "Unauthorized: No authentication found"));
            }
            // Long currentUserId = userService.getUserByEmail(auth.getName()).getId();
            // TODO: Add verification that utId belongs to currentUserId
            
            return ResponseEntity.ok(personalizationService.getPersonalizedDayDetails(utId, dayNumber));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }

    /**
     * GET /api/user/personalized/today?dayNumber=5
     * Lấy bài tập cá nhân hóa cho hôm nay
     * Video URL được lấy từ Challenge entity
     */
    @GetMapping("/personalized/today")
    public ResponseEntity<NotificationResponse> getTodayPersonalizedWorkout(
            @RequestParam(required = false) Integer dayNumber,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).body(
                    new NotificationResponse(false, "Unauthorized")
                );
            }

            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            
            // Nếu không có dayNumber, mặc định là day 1
            if (dayNumber == null) {
                dayNumber = 1;
            }

            return ResponseEntity.ok(
                personalizationService.getTodayPersonalizedWorkout(userId, dayNumber)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }

    /**
     * POST /api/user/training/{utId}/regenerate-personalized
     * Tạo lại PersonalizedPlanDetail cho một UserTraining đã tồn tại
     * Dùng khi PersonalizedPlanDetail chưa được tạo hoặc cần tạo lại
     */
    @PostMapping("/training/{utId}/regenerate-personalized")
    public ResponseEntity<NotificationResponse> regeneratePersonalizedPlanDetails(
            @PathVariable Long utId) {
        try {
            // Verify user owns this training plan
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                return ResponseEntity.ok(new NotificationResponse(false, "Unauthorized: No authentication found"));
            }
            // Long currentUserId = userService.getUserByEmail(auth.getName()).getId();
            // TODO: Add verification that utId belongs to currentUserId
            
            // Delete existing personalized details first (if any)
            // Then create new ones
            NotificationResponse response = personalizationService.createPersonalizedPlanDetails(utId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }
}


