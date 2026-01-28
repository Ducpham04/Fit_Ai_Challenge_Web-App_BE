package com.example.fitchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformationBodyDTO {
    private Long infoId;
    private Long userId ;
    private String userName;
    private BigDecimal heightCm;
    private BigDecimal weightKg;
    private Integer age;
    private String gender;
    private BigDecimal bodyFatPct;
    private BigDecimal bmi;
    private Long goalId;
    private String goalName;
    private String createdAt;

}
