package com.example.fitchallenge.DTO.UserTrainingDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UserRequestDTO {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be a positive number")
    private Long userID;

    @NotNull(message = "Training plan ID is required")
    @Min(value = 1, message = "Training plan ID must be a positive number")
    private Long trainingID;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

}
