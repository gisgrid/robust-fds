package com.frauddetection.service;

import com.frauddetection.model.Transaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FraudDetectionServiceTest {

    @InjectMocks
    private FraudDetectionService fraudDetectionService;

    @BeforeEach
    void setUp() {
        // Set up test configuration matching configmap.yaml
        ReflectionTestUtils.setField(fraudDetectionService, "amountThreshold", "10000");
        ReflectionTestUtils.setField(fraudDetectionService, "suspiciousAccounts", "ACC-001,ACC-002,ACC-003");
        fraudDetectionService.init();
    }

    @Test
    @Order(1)
    @DisplayName("1.1 Test Normal Transaction")
    void testNormalTransaction() {
        // Use a non-suspicious account (ACC-004 is not in the suspicious list)
        Transaction transaction = createTransaction("TXN-001", "ACC-004", new BigDecimal("5000"));
        fraudDetectionService.processTransaction(transaction);
        assertEquals(Transaction.TransactionStatus.COMPLETED, transaction.getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("1.2 Test High Amount Transaction")
    void testHighAmountTransaction() {
        // Reset configuration to default values
        ReflectionTestUtils.setField(fraudDetectionService, "amountThreshold", "10000");
        ReflectionTestUtils.setField(fraudDetectionService, "suspiciousAccounts", "ACC-001,ACC-002,ACC-003");
        fraudDetectionService.init();
        
        // Use a non-suspicious account with high amount
        Transaction transaction = createTransaction("TXN-002", "ACC-004", new BigDecimal("15000"));
        fraudDetectionService.processTransaction(transaction);
        assertEquals(Transaction.TransactionStatus.FRAUDULENT, transaction.getStatus());
    }

    @Test
    @Order(3)
    @DisplayName("1.3 Test Suspicious Account Transaction")
    void testSuspiciousAccountTransaction() {
        // Test with each suspicious account from the config
        String[] suspiciousAccounts = {"ACC-001", "ACC-002", "ACC-003"};
        for (String accountId : suspiciousAccounts) {
            Transaction transaction = createTransaction("TXN-003-" + accountId, accountId, new BigDecimal("100"));
            fraudDetectionService.processTransaction(transaction);
            assertEquals(Transaction.TransactionStatus.FRAUDULENT, transaction.getStatus(),
                    "Transaction from suspicious account " + accountId + " should be marked as fraudulent");
        }
    }

    @Test
    @Order(4)
    @DisplayName("1.4 Test Invalid Amount Threshold")
    void testInvalidAmountThreshold() {
        // Test with invalid amount threshold
        ReflectionTestUtils.setField(fraudDetectionService, "amountThreshold", "invalid");
        ReflectionTestUtils.setField(fraudDetectionService, "suspiciousAccounts", "ACC-001,ACC-002,ACC-003");
        fraudDetectionService.init();
        
        // Should use default value (10000)
        Transaction transaction = createTransaction("TXN-004", "ACC-004", new BigDecimal("15000"));
        fraudDetectionService.processTransaction(transaction);
        assertEquals(Transaction.TransactionStatus.FRAUDULENT, transaction.getStatus());
    }

    @Test
    @Order(5)
    @DisplayName("1.5 Test Empty Suspicious Accounts")
    void testEmptySuspiciousAccounts() {
        // Test with empty suspicious accounts
        ReflectionTestUtils.setField(fraudDetectionService, "suspiciousAccounts", "");
        fraudDetectionService.init();
        
        // Test with a previously suspicious account
        Transaction transaction = createTransaction("TXN-005", "ACC-001", new BigDecimal("100"));
        fraudDetectionService.processTransaction(transaction);
        assertEquals(Transaction.TransactionStatus.COMPLETED, transaction.getStatus());
    }

    private Transaction createTransaction(String transactionId, String accountId, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
} 