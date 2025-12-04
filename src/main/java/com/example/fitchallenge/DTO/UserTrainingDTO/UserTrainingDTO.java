package com.example.fitchallenge.DTO.UserTrainingDTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserTrainingDTO {
    private Long id;
    private String name ;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private Double completionPercentage ;
    private String Status ;

}
