package com.example.fitchallenge.DTO.ChallengeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO cho Challenge response theo yêu cầu FE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeResponseDTO {
    private Long id;
    private String title;
    private String description;
    private List<String> video; // Array of video URLs
    private String difficulty; // Easy, Medium, Hard
    private Integer participants; // Count of participants
    private String reward; // Reward description
    private String status; // Active, Upcoming, Completed
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // For challenge detail with participants list
    private List<ParticipantDTO> participantsList;
}



