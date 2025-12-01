package com.example.FIT_Challenge.DTO.UserTrainingDTO;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UserRequestDTO {
    private Long userID  ;
    private Long trainingID ;
    private LocalDate startDate ;
    private LocalDate endDate ;

}
