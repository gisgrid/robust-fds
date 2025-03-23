package com.frauddetection.service;

import com.frauddetection.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class FraudDetectionService {
    
    @Value("${AMOUNT_THRESHOLD:10000}")
    private String amountThreshold;
    
    @Value("${SUSPICIOUS_ACCOUNTS:}")
    private String suspiciousAccounts;
    
    private BigDecimal suspiciousAmountThreshold;
    private Set<String> suspiciousAccountSet;

    @PostConstruct
    public void init() {
        // Initialize amount threshold
        try {
            suspiciousAmountThreshold = new BigDecimal(amountThreshold);
            log.info("Configured amount threshold: {}", suspiciousAmountThreshold);
        } catch (NumberFormatException e) {
            log.error("Invalid amount threshold: {}. Using default value: 10000", amountThreshold);
            suspiciousAmountThreshold = new BigDecimal("10000");
        }

        // Initialize suspicious accounts set
        suspiciousAccountSet = new HashSet<>();
        if (suspiciousAccounts != null && !suspiciousAccounts.trim().isEmpty()) {
            suspiciousAccountSet.addAll(Arrays.asList(suspiciousAccounts.split(",")));
            log.info("Configured suspicious accounts: {}", suspiciousAccountSet);
        }
    }

    public boolean isFraudulent(Transaction transaction) {
        // Rule 1: Check for suspicious amount
        if (transaction.getAmount().compareTo(suspiciousAmountThreshold) > 0) {
            log.warn("Suspicious transaction detected: Amount exceeds threshold. Transaction ID: {}", 
                    transaction.getTransactionId());
            return true;
        }

        // Rule 2: Check for suspicious account
        if (suspiciousAccountSet.contains(transaction.getAccountId())) {
            log.warn("Suspicious transaction detected: Account is in suspicious list. Transaction ID: {}", 
                    transaction.getTransactionId());
            return true;
        }

        return false;
    }

    public void processTransaction(Transaction transaction) {
        if (isFraudulent(transaction)) {
            transaction.setStatus(Transaction.TransactionStatus.FRAUDULENT);
            log.error("Fraudulent transaction detected and blocked: {}", transaction);
        } else {
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            log.info("Transaction processed successfully: {}", transaction);
        }
    }
} 