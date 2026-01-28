package com.example.fitchallenge.controller.user;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserChallengeService;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller cho User Challenge APIs (user-facing)
 */
@RestController
@RequestMapping("/api/user/challenges")
@RequiredArgsConstructor
public class UserChallengeController {

    private final UserChallengeService userChallengeService;
    private final UserService userService;

    /**
     * PUT /api/user/challenges/{id}/complete
     * Đánh dấu challenge đã hoàn thành
     * User chỉ có thể đánh dấu challenge của chính mình
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<NotificationResponse> completeChallenge(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }
        
        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            NotificationResponse response = userChallengeService.completeChallenge(id, userId);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }

    /**
     * GET /api/user/challenges/my
     * Lấy danh sách challenges của user hiện tại
     */
    @GetMapping("/my")
    public ResponseEntity<NotificationResponse> getMyChallenges(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String status) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }
        
        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            
            // TODO: Implement getMyChallenges method in service
            // Hiện tại có thể dùng getAll và filter
            NotificationResponse allChallenges = userChallengeService.getAll();
            
            // Filter by userId and status if provided
            if (allChallenges.getData() instanceof java.util.List) {
                java.util.List<?> challenges = (java.util.List<?>) allChallenges.getData();
                java.util.List<?> filtered = challenges.stream()
                    .filter(c -> {
                        try {
                            java.lang.reflect.Method getUserId = c.getClass().getMethod("getUserId");
                            Long challengeUserId = (Long) getUserId.invoke(c);
                            if (!challengeUserId.equals(userId)) return false;
                            
                            if (status != null) {
                                java.lang.reflect.Method getStatus = c.getClass().getMethod("getStatus");
                                String challengeStatus = (String) getStatus.invoke(c);
                                return status.equalsIgnoreCase(challengeStatus);
                            }
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(java.util.stream.Collectors.toList());
                
                return ResponseEntity.ok(new NotificationResponse(true, "My challenges retrieved", filtered));
            }
            
            return ResponseEntity.ok(allChallenges);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }
}

