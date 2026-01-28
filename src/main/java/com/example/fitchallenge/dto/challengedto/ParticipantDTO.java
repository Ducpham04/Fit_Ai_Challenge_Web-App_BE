package com.example.fitchallenge.dto.challengedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * DTO cho participant trong challenge
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantDTO {
    private Long userId;
    private String userName;
    private OffsetDateTime joinedAt;
    private Integer progress; // Percentage 0-100
    private Boolean completed;
}



