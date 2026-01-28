package com.example.fitchallenge.dto.users;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String userName;
    private String email;
    private String linkImage; // URL của avatar image
}

