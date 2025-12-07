package com.example.fitchallenge.controller.User;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.PersonalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/training")
@RequiredArgsConstructor
public class PersonalizedTrainingController {

    private final PersonalizationService personalizationService;

    /**
     * GET /api/user/training/{utId}/day/{dayNumber}
     * Lấy danh sách bài tập đã được cá nhân hóa cho một ngày cụ thể
     */
    @GetMapping("/{utId}/day/{dayNumber}")
    public ResponseEntity<NotificationResponse> getPersonalizedDayDetails(
            @PathVariable Long utId,
            @PathVariable Integer dayNumber) {
        try {
            // Verify user owns this training plan
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long currentUserId = Long.parseLong(auth.getName());
            
            // TODO: Add verification that utId belongs to currentUserId
            
            return ResponseEntity.ok(personalizationService.getPersonalizedDayDetails(utId, dayNumber));
        } catch (Exception e) {
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }
}

