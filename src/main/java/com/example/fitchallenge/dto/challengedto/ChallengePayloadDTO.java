package com.example.fitchallenge.dto.challengedto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  ChallengePayloadDTO {
    private Long goalId;
    private String title;
    private String description;
    private String difficult;
    private String linkVideos ;
    private String status;
    private String exerciseType; // AI model/exercise type: push-up, squat, pull-up, sit-up, plank

}
