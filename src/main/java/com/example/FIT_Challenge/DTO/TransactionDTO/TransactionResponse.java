package com.example.FIT_Challenge.DTO.TransactionDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
  
public class TransactionResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String type;
    private BigDecimal amount;
    private Integer points;
    private String reference;
    private String status;
    private String description;
    private ZonedDateTime createdAt;

    public TransactionResponse(Long id, Long id1, String userName, String type, BigDecimal amount, Integer points, String reference, String status, String description, ZonedDateTime createdAt) {
    }
}
