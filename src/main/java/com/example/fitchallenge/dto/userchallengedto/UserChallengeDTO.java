package com.example.fitchallenge.dto.userchallengedto;

import lombok.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeDTO {
    private Long ucId;
    private Long userId;
    private Long challengeId;
    private String status;
    private String videoUrl;
    private String keypointsPayload;
    private Integer score;
    private Double confidence;
    private ZonedDateTime submittedAt;
    private ZonedDateTime completedAt;

    public UserChallengeDTO(Long ucId, Long id, Long id1, String status, String videoUrl, String keypointsPayload, Integer score, Double confidence) {
    }


}
