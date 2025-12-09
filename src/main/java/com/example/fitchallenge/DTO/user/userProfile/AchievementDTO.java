package com.example.fitchallenge.DTO.user.userProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Thành tích
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementDTO {
    private String name;
    private String icon;   // emoji hoặc icon code
    private String color;  // gradient tailwind class
}