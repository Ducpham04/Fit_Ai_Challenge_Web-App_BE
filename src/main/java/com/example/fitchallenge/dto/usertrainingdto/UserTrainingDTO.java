package com.example.fitchallenge.dto.usertrainingdto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserTrainingDTO {
    private Long id; // utId (UserTraining ID)
    private Long trainingPlanId; // Training Plan template ID
    private String name ;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private Double completionPercentage ;
    private String Status ;

}
