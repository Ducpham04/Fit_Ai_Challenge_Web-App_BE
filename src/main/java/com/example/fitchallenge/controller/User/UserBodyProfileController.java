package com.example.fitchallenge.controller.User;

import com.example.fitchallenge.DTO.UserBodyProfileDTO.UserBodyProfileRequest;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserBodyProfileService;
import com.example.fitchallenge.service.UserService;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createOrUpdateBodyProfile(
            @RequestBody UserBodyProfileRequest request) {
        try {
            // Get current user ID from JWT (auth.getName() returns email)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                return ResponseEntity.ok(new NotificationResponse(false, "Unauthorized: No authentication found"));
            }
            Long userId = userService.getUserByEmail(auth.getName()).getId();
            
            return ResponseEntity.ok(userBodyProfileService.createOrUpdateBodyProfile(userId, request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<NotificationResponse> getBodyProfile() {
        try {
            // Get current user ID from JWT (auth.getName() returns email)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                return ResponseEntity.ok(new NotificationResponse(false, "Unauthorized: No authentication found"));
            }
            Long userId = userService.getUserByEmail(auth.getName()).getId();
            
            return ResponseEntity.ok(userBodyProfileService.getBodyProfile(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new NotificationResponse(false, "Error: " + e.getMessage()));
        }
    }
}


