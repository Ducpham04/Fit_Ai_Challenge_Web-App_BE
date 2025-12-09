package com.example.fitchallenge.controller.User;

import com.example.fitchallenge.DTO.BodyMetricHistoryDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.BodyMetricHistoryService;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/user/body-metric")
@RequiredArgsConstructor
public class BodyMetricHistoryController {

    private final BodyMetricHistoryService bodyMetricHistoryService;
    private final UserService userService;

    /**
     * POST /api/user/body-metric
     * Thêm lịch sử body metric
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> createBodyMetric(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BodyMetricHistoryDTO dto) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }

        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            NotificationResponse response = bodyMetricHistoryService.createBodyMetric(userId, dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }

    /**
     * GET /api/user/body-metric?from=2024-01-01&to=2024-02-01
     * Lấy lịch sử body metric trong khoảng thời gian
     */
    @GetMapping
    public ResponseEntity<NotificationResponse> getBodyMetricsByDateRange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }

        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            
            // Nếu không có from/to, lấy tất cả
            if (from == null && to == null) {
                NotificationResponse response = bodyMetricHistoryService.getAllBodyMetrics(userId);
                return ResponseEntity.ok(response);
            }
            
            // Mặc định from = 30 ngày trước, to = hiện tại
            if (from == null) {
                from = ZonedDateTime.now().minusDays(30);
            }
            if (to == null) {
                to = ZonedDateTime.now();
            }
            
            NotificationResponse response = bodyMetricHistoryService.getBodyMetricsByDateRange(userId, from, to);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }

    /**
     * GET /api/user/body-metric/latest
     * Lấy bản ghi mới nhất
     */
    @GetMapping("/latest")
    public ResponseEntity<NotificationResponse> getLatestBodyMetric(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }

        try {
            Long userId = userService.getUserByEmail(userDetails.getUsername()).getId();
            NotificationResponse response = bodyMetricHistoryService.getLatestBodyMetric(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error: " + e.getMessage())
            );
        }
    }
}





