package com.example.fitchallenge.DTO.TrainingPlanDetailDTO;

import com.example.fitchallenge.DTO.ChallengeDTO.ChallengeDTOPayload;
import com.example.fitchallenge.Entity.Challenges;
import lombok.Data;

@Data
public class TrainingPlanDetailResponse {
    private Long tpdId;
    private Long trainingPlanId;
    private String trainingPlanTitle;
    private Integer dayNumber;
    private ChallengeDTOPayload challenge;
    private String challengeName;
    private Integer sets;
    private Integer reps;
}
