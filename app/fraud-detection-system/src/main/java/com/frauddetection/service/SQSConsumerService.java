package com.frauddetection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frauddetection.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSConsumerService {
    private final SqsClient sqsClient;
    private final FraudDetectionService fraudDetectionService;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.transaction-queue-url}")
    private String transactionQueueUrl;

    @Value("${aws.sqs.fraud-alert-queue-url}")
    private String fraudAlertQueueUrl;

    @Scheduled(fixedDelay = 1000) // Poll every second
    public void pollTransactions() {
        try {
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(transactionQueueUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(20) // Enable long polling
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
            
            for (Message message : messages) {
                try {
                    Transaction transaction = objectMapper.readValue(message.body(), Transaction.class);
                    log.info("Received transaction: {}", transaction);
                    
                    // Process the transaction
                    fraudDetectionService.processTransaction(transaction);
                    
                    // Log the detection result
                    if (transaction.getStatus() == Transaction.TransactionStatus.FRAUDULENT) {
                        log.warn("Fraud detection result - Transaction ID: {}, Status: FRAUDULENT, Reason: Amount exceeds threshold or high frequency", 
                                transaction.getTransactionId());
                        sendFraudAlert(transaction);
                    } else {
                        log.info("Fraud detection result - Transaction ID: {}, Status: COMPLETED, No suspicious activity detected", 
                                transaction.getTransactionId());
                    }
                    
                    // Delete the message after successful processing
                    deleteMessage(message.receiptHandle());
                    
                } catch (Exception e) {
                    log.error("Error processing message: {}", message.body(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error polling SQS queue", e);
        }
    }

    private void sendFraudAlert(Transaction transaction) {
        try {
            String messageBody = objectMapper.writeValueAsString(transaction);
            SendMessageRequest sendRequest = SendMessageRequest.builder()
                    .queueUrl(fraudAlertQueueUrl)
                    .messageBody(messageBody)
                    .build();
            
            sqsClient.sendMessage(sendRequest);
            log.info("Sent fraud alert for transaction: {}", transaction.getTransactionId());
        } catch (Exception e) {
            log.error("Error sending fraud alert", e);
        }
    }

    private void deleteMessage(String receiptHandle) {
        try {
            DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                    .queueUrl(transactionQueueUrl)
                    .receiptHandle(receiptHandle)
                    .build();
            
            sqsClient.deleteMessage(deleteRequest);
        } catch (Exception e) {
            log.error("Error deleting message", e);
        }
    }
} 