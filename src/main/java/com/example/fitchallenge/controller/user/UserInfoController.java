package com.example.fitchallenge.controller.user;

import com.example.fitchallenge.dto.UserInfoDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserInfoService;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserService userService;

    /**
     * GET /api/user/info
     * Lấy thông tin user (body info) của user hiện tại
     */
    @GetMapping("/info")
    public ResponseEntity<NotificationResponse> getUserInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }

        try {
            // Lấy userId từ email
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            NotificationResponse response = userInfoService.getUserInfo(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }

    /**
     * PUT /api/user/info
     * Cập nhật thông tin body của user
     * Tự động tính BMI, BMR, recommended calories
     */
    @PutMapping("/info")
    public ResponseEntity<NotificationResponse> updateUserInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserInfoDTO dto) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }

        try {
            // Lấy userId từ email - chỉ cho phép user cập nhật thông tin của chính mình
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            
            // Đảm bảo userId trong DTO khớp với user hiện tại
            if (dto.getUserId() != null && !dto.getUserId().equals(userId)) {
                return ResponseEntity.status(403).body(
                    new NotificationResponse(false, "You can only update your own information")
                );
            }
            
            NotificationResponse response = userInfoService.updateUserInfo(userId, dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }
}





