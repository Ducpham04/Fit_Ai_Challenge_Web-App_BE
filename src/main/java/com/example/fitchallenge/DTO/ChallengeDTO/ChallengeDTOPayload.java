package com.example.fitchallenge.DTO.ChallengeDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  ChallengeDTOPayload {
    private Long goalId;
    private String title;
    private String description;
    private String difficult;
    private String linkVideos ;
    private String status;

}
