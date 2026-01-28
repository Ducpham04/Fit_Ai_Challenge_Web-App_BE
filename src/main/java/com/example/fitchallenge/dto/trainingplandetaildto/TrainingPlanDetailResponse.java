package com.example.fitchallenge.dto.trainingplandetaildto;

import com.example.fitchallenge.dto.challengedto.ChallengePayloadDTO;
import lombok.Data;

@Data
public class TrainingPlanDetailResponse {
    private Long tpdId;
    private Long trainingPlanId;
    private String trainingPlanTitle;
    private Integer dayNumber;
    private ChallengePayloadDTO challenge;
    private String challengeName;
    private Integer sets;
    private Integer reps;
}
