package com.example.fitchallenge.controller;

import com.example.fitchallenge.DTO.LeaderboardDTO;
import com.example.fitchallenge.service.LeaderboardService;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller cho Leaderboard API
 * Endpoint: GET /api/leaderboard
 */
@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;
    private final UserService userService;

    /**
     * GET /api/leaderboard
     * Lấy global leaderboard
     * 
     * @param category Loại ranking: "points" (default), "challenges", hoặc "streak"
     * @param period Khoảng thời gian: "all-time" (default), "weekly", hoặc "monthly"
     * @param limit Số lượng entries trả về (default: 50)
     * @return Danh sách leaderboard entries
     */
    @GetMapping
    public ResponseEntity<List<LeaderboardDTO.LeaderboardEntryDTO>> getGlobalLeaderboard(
            @RequestParam(required = false, defaultValue = "points") String category,
            @RequestParam(required = false, defaultValue = "all-time") String period,
            @RequestParam(required = false, defaultValue = "50") Integer limit
    ) {
        // Lấy current user ID từ authentication (nếu có)
        Long currentUserId = null;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !"anonymousUser".equals(auth.getName())) {
                // auth.getName() returns email, get user ID from email
                currentUserId = userService.getUserByEmail(auth.getName()).getId();
            }
        } catch (Exception e) {
            // If we can't get current user, continue without it (leaderboard still works)
            System.out.println("Could not get current user ID (leaderboard still works): " + e.getMessage());
        }

        List<LeaderboardDTO.LeaderboardEntryDTO> leaderboard = 
                leaderboardService.getGlobalLeaderboard(category, period, limit, currentUserId);

        return ResponseEntity.ok(leaderboard);
    }
}

