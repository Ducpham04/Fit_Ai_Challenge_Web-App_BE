package com.example.fitchallenge.DTO.user;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(
            message = "Please provide a valid email address",
            flags = Pattern.Flag.CASE_INSENSITIVE   // makes validation case-insensitive (recommended)
    )
    @Size(
            max = 255,
            message = "Email must not exceed 255 characters"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            max = 128,
            message = "Password must be between 8 and 128 characters"
    )
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;
    }
