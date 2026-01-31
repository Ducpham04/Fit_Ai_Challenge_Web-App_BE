package com.example.fitchallenge.DTO.user;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Username can only contain letters, numbers, underscores, dots and hyphens")
    private String userName;

    @Email(message = "Please enter a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(https?://.*\\.(png|jpg|jpeg|gif|webp))$|^$",
            message = "Link image must be a valid image URL (png, jpg, jpeg, gif, webp) or empty"
    )
    private String linkImage;
}

