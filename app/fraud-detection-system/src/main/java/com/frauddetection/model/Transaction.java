package com.frauddetection.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {
    private String transactionId;
    private String accountId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime timestamp;
    private String merchantId;
    private String location;
    private TransactionType type;
    private TransactionStatus status;

    public enum TransactionType {
        PURCHASE,
        WITHDRAWAL,
        TRANSFER,
        DEPOSIT
    }

    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        FRAUDULENT
    }
} 