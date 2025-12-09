package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.BodyMetricHistoryDTO;
import com.example.fitchallenge.config.NotificationResponse;

import java.time.ZonedDateTime;

public interface BodyMetricHistoryService {
    /**
     * Thêm lịch sử body metric
     */
    NotificationResponse createBodyMetric(Long userId, BodyMetricHistoryDTO dto);
    
    /**
     * Lấy lịch sử body metric trong khoảng thời gian
     */
    NotificationResponse getBodyMetricsByDateRange(Long userId, ZonedDateTime fromDate, ZonedDateTime toDate);
    
    /**
     * Lấy bản ghi mới nhất
     */
    NotificationResponse getLatestBodyMetric(Long userId);
    
    /**
     * Lấy tất cả lịch sử body metric của user
     */
    NotificationResponse getAllBodyMetrics(Long userId);
}

