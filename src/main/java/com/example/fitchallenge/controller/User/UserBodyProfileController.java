package com.example.fitchallenge.controller.User;

import com.example.fitchallenge.DTO.UserBodyProfileDTO.UserBodyProfileRequest;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserBodyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile/body")
@RequiredArgsConstructor
public class UserBodyProfileController {

    private final UserBodyProfileService userBodyProfileService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createOrUpdateBodyProfile(
            @RequestBody UserBodyProfileRequest request) {
        try {
            // Get current user ID from JWT
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long userId = Long.parseLong(auth.getName());
            
            return ResponseEntity.ok(userBodyProfileService.createOrUpdateBodyProfile(userId, request));
        } catch (Exception e) {
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<NotificationResponse> getBodyProfile() {
        try {
            // Get current user ID from JWT
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long userId = Long.parseLong(auth.getName());
            
            return ResponseEntity.ok(userBodyProfileService.getBodyProfile(userId));
        } catch (Exception e) {
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }
}

