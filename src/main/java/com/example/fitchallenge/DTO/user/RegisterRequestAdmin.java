package com.example.fitchallenge.DTO.user;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestAdmin {
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role ID is required")
    @Min(value = 1, message = "Role ID must be greater than 0")
    private Long roleId;
}
