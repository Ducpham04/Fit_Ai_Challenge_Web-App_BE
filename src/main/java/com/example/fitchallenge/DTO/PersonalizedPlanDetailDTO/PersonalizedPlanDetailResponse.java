package com.example.fitchallenge.DTO.PersonalizedPlanDetailDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPlanDetailResponse {
    private Long ppdId;
    private Long utId;
    private Long tpdId;
    private Integer dayNumber;
    private String challengeName;
    private Integer defaultReps;
    private Integer defaultSets;
    private Integer defaultDuration;
    private Integer customReps;
    private Integer customTime;
    private Double customDistance;
    private String exerciseVariant;
    private Integer intensityLevel;
    private String note;
}

