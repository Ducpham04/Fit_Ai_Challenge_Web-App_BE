package com.example.fitchallenge.dto.transactiondto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Long userId;
    private String type;
    private BigDecimal amount;
    private Integer points;
    private String reference;
    private String description;
    private String status ;
}
