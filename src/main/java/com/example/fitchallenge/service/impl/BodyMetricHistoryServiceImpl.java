package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.dto.BodyMetricHistoryDTO;
import com.example.fitchallenge.entity.BodyMetricHistory;
import com.example.fitchallenge.entity.User;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.BodyMetricHistoryRepository;
import com.example.fitchallenge.repository.user.UserRepository;
import com.example.fitchallenge.service.BodyMetricHistoryService;
import com.example.fitchallenge.utils.BodyMetricsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BodyMetricHistoryServiceImpl implements BodyMetricHistoryService {

    private final BodyMetricHistoryRepository bodyMetricHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponse createBodyMetric(Long userId, BodyMetricHistoryDTO dto) {
        try {
            // Kiểm tra user tồn tại
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new NotificationResponse(false, "User not found");
            }
            User user = userOpt.get();

            // Validate required fields
            if (dto.getWeightKg() == null) {
                return new NotificationResponse(false, "Weight is required");
            }

            // Tạo entity
            BodyMetricHistory bodyMetric = BodyMetricHistory.builder()
                    .user(user)
                    .weightKg(dto.getWeightKg())
                    .heightCm(dto.getHeightCm())
                    .bodyFatPct(dto.getBodyFatPct())
                    .muscleMassKg(dto.getMuscleMassKg())
                    .waterPct(dto.getWaterPct())
                    .notes(dto.getNotes())
                    .recordedAt(dto.getRecordedAt() != null ? dto.getRecordedAt() : ZonedDateTime.now())
                    .createdAt(ZonedDateTime.now())
                    .build();

            // Tính BMI nếu có weight và height
            if (dto.getWeightKg() != null && dto.getHeightCm() != null) {
                BigDecimal bmi = BodyMetricsCalculator.calculateBMI(
                    dto.getWeightKg(), 
                    dto.getHeightCm()
                );
                bodyMetric.setBmi(bmi);
            }

            // Lưu
            bodyMetricHistoryRepository.save(bodyMetric);

            // Trả về DTO
            BodyMetricHistoryDTO responseDto = toDto(bodyMetric);
            return new NotificationResponse(true, "Body metric created successfully", responseDto);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error creating body metric: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getBodyMetricsByDateRange(Long userId, ZonedDateTime fromDate, ZonedDateTime toDate) {
        try {
            List<BodyMetricHistory> metrics = bodyMetricHistoryRepository.findByUserIdAndDateRange(
                userId, fromDate, toDate
            );
            
            List<BodyMetricHistoryDTO> dtoList = metrics.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            
            return new NotificationResponse(true, "Body metrics retrieved successfully", dtoList);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error retrieving body metrics: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getLatestBodyMetric(Long userId) {
        try {
            Optional<BodyMetricHistory> metricOpt = bodyMetricHistoryRepository.findLatestByUserId(userId);
            
            if (metricOpt.isEmpty()) {
                return new NotificationResponse(false, "No body metric found for user");
            }
            
            BodyMetricHistoryDTO dto = toDto(metricOpt.get());
            return new NotificationResponse(true, "Latest body metric retrieved successfully", dto);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error retrieving latest body metric: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getAllBodyMetrics(Long userId) {
        try {
            List<BodyMetricHistory> metrics = bodyMetricHistoryRepository.findByUserIdOrderByRecordedAtDesc(userId);
            
            List<BodyMetricHistoryDTO> dtoList = metrics.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            
            return new NotificationResponse(true, "All body metrics retrieved successfully", dtoList);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error retrieving body metrics: " + e.getMessage());
        }
    }

    private BodyMetricHistoryDTO toDto(BodyMetricHistory metric) {
        BodyMetricHistoryDTO dto = new BodyMetricHistoryDTO();
        dto.setBmhId(metric.getBmhId());
        dto.setUserId(metric.getUser().getId());
        dto.setWeightKg(metric.getWeightKg());
        dto.setHeightCm(metric.getHeightCm());
        dto.setBmi(metric.getBmi());
        dto.setBodyFatPct(metric.getBodyFatPct());
        dto.setMuscleMassKg(metric.getMuscleMassKg());
        dto.setWaterPct(metric.getWaterPct());
        dto.setNotes(metric.getNotes());
        dto.setRecordedAt(metric.getRecordedAt());
        dto.setCreatedAt(metric.getCreatedAt());
        return dto;
    }
}

