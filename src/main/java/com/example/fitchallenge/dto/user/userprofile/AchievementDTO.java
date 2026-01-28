package com.example.fitchallenge.dto.user.userprofile;

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