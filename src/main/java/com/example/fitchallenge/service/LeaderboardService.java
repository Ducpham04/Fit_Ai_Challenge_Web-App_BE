package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.LeaderboardDTO;

import java.util.List;

/**
 * Service interface cho Leaderboard
 */
public interface LeaderboardService {
    
    /**
     * Lấy global leaderboard
     * 
     * @param category Loại ranking: "points", "challenges", hoặc "streak"
     * @param period Khoảng thời gian: "all-time", "weekly", hoặc "monthly"
     * @param limit Số lượng entries trả về
     * @param currentUserId ID của user hiện tại (để đánh dấu isCurrentUser)
     * @return Danh sách leaderboard entries
     */
    List<LeaderboardDTO.LeaderboardEntryDTO> getGlobalLeaderboard(
        String category,
        String period,
        Integer limit,
        Long currentUserId
    );
}

