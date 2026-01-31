package com.example.fitchallenge.DTO.RewardDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RewardRequest {
    @NotBlank(message = "Image link is required")
    @Size(max = 500, message = "Image link must not exceed 500 characters")
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Image link must be a valid URL"
    )
    private String linkImage;

    @NotBlank(message = "Reward name is required")
    @Size(max = 255, message = "Reward name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Cost points is required")
    @Min(value = 0, message = "Cost points must be greater than or equal to 0")
    private Integer costPoints;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;

    @Size(max = 255, message = "External partner must not exceed 255 characters")
    private String externalPartner;
}
