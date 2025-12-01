package com.example.FIT_Challenge.DTO.UserTrainingDTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserTrainingDTO {
    private Long id;
    private String name ;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private Double completionPercentage ;
    private String Status ;

}
