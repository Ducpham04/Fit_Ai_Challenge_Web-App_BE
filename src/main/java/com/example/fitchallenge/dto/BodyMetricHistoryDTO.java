package com.example.fitchallenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO cho Body Metric History API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyMetricHistoryDTO {
    private Long bmhId;
    private Long userId;
    
    // Metrics
    private BigDecimal weightKg;
    private BigDecimal heightCm;
    private BigDecimal bmi;
    private BigDecimal bodyFatPct;
    private BigDecimal muscleMassKg;
    private BigDecimal waterPct;
    private String notes;
    
    // Timestamps - Accept ISO string format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime recordedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime createdAt;
}

