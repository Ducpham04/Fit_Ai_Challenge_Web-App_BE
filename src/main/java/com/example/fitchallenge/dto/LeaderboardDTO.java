package com.example.fitchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO cho Leaderboard API
 */
public class LeaderboardDTO {

    /**
     * Leaderboard Entry - Mỗi entry đại diện cho 1 user trong leaderboard
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaderboardEntryDTO {
        private Integer rank;
        private Long userId;
        private String userName;
        private String profileImage;
        private Integer value; // Giá trị theo category (points, challenges, hoặc streak)
        private Integer aiScore;
        private Integer challengesCompleted;
        private Integer streak;
        private Boolean isCurrentUser;
    }

    /**
     * Response cho GET /api/leaderboard
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaderboardResponse {
        private List<LeaderboardEntryDTO> leaderboard;
        private String category;
        private String period;
        private Integer total;
        private Integer limit;
    }
}

