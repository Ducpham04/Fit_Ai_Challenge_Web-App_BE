package com.example.fitchallenge.dto.personalizedplandetaildto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response cho PersonalizedPlanDetail
 * Video URL được lấy từ Challenge entity thông qua challengeId
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPlanDetailResponse {
    /**
     * Mã bản ghi PersonalizedPlanDetail
     */
    private Long id;
    
    /**
     * User ID
     */
    private Long userId;
    
    /**
     * Số ngày trong kế hoạch
     */
    private Integer dayNumber;
    
    /**
     * Challenge ID - dùng để lấy video từ Challenge entity
     */
    private Long challengeId;
    
    /**
     * Tên bài tập
     */
    private String exerciseName;
    
    /**
     * Số hiệp (sets)
     */
    private Integer sets;
    
    /**
     * Số lần lặp (reps)
     */
    private Integer reps;
    
    /**
     * Độ khó (EASY, MEDIUM, HARD)
     */
    private String difficulty;
    
    /**
     * Nhóm cơ mục tiêu
     */
    private String targetMuscle;
    
    /**
     * Video URL - được lấy từ Challenge.linkVideos
     */
    private String videoUrl;
    
    /**
     * Tên challenge (từ Challenge.title)
     */
    private String challengeName;

    /**
     * Calories ước tính cho bài tập này
     */
    private Integer estimatedCalories;
}


