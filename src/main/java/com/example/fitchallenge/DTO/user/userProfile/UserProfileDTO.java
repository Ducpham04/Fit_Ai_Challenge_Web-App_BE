package com.example.fitchallenge.DTO.user.userProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Thông tin cơ bản của user
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long id;
    private String email;
    private String username;
    private String avatar;
    private String joinDate; // ISO string, ví dụ: 2025-12-03T00:00:00Z
    private Integer currentStreak; // số ngày liên tục
}