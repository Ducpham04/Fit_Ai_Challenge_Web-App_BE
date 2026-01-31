package com.example.fitchallenge.DTO.TransactionDTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be a positive number")
    private Long userId;

    @NotBlank(message = "Transaction type is required")
    @Pattern(
            regexp = "^(DEPOSIT|WITHDRAWAL|TRANSFER|PAYMENT|REFUND|PURCHASE)$",
            message = "Invalid transaction type. Allowed: DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, REFUND, PURCHASE"
    )
    private String type;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than or equal to 0.01")
    @Digits(integer = 12, fraction = 2, message = "Amount can have max 12 digits before decimal and 2 after")
    private BigDecimal amount;

    @Min(value = 0, message = "Points cannot be negative")
    @Max(value = 1000000, message = "Points cannot exceed 1,000,000")
    private Integer points;

    @Size(max = 100, message = "Reference cannot exceed 100 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\-_.]+$",
            message = "Reference can only contain letters, numbers, hyphens, dots and underscores"
    )
    private String reference;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    private String status ;
}
