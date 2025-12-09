package com.example.fitchallenge.DTO.UserBodyProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyProfileResponse {
    private Long id;
    private Long userId;
    private Double height;
    private Double weight;
    private Double bmi;
    private Double bodyFat;
    private Double muscleMass;
    private Integer age;
    private String gender;
    private String experienceLevel;
    private String goal;
    private String injuryNotes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}




