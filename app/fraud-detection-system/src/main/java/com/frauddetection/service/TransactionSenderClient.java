package com.frauddetection.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionSenderClient {

    private static final String QUEUE_NAME = "transaction-queue"; // change if needed
    private static final String REGION = "ap-southeast-2"; // your region
    private static final String ENDPOINT = "https://sqs.ap-southeast-2.amazonaws.com"; // use your endpoint
    private static final String AWS_ACCESS_KEY = "<your-access-key>";
    private static final String AWS_SECRET_KEY = "<your-secret-key>";

    public static void main(String[] args) throws InterruptedException {
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.of(REGION))
                .endpointOverride(URI.create(ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY)))
                .build();

        String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                .queueName(QUEUE_NAME)
                .build()).queueUrl();

        System.out.println("[Sender] Starting transaction sender loop...");

        int counter = 1;
        while (true) {
            String transactionId = "tx-test-" + String.format("%03d", counter);
            String accountId = "acc-" + counter;
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
		String messageBody = String.format("{"
                    + "\"transactionId\":\"%s\"," 
                    + "\"accountId\":\"%s\"," 
                    + "\"amount\":15000," 
                    + "\"currency\":\"USD\"," 
                    + "\"timestamp\":\"%s\"," 
                    + "\"merchantId\":\"m-abc123\"," 
                    + "\"location\":\"Sydney-AU\"," 
                    + "\"type\":\"PURCHASE\"," 
                    + "\"status\":\"PENDING\"}" ,
                    transactionId, accountId, timestamp);
			
			messageBody = String.format("{\"transaction_id\":\"%s\",\"account_id\":\"%s\",\"amount\":15000}",
                    transactionId, accountId);

            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build());

            timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.printf("[Sender] Sent transaction %s at %s\n", transactionId, timestamp);

            counter++;
            Thread.sleep(1000); // send every second
            
			
	        if(counter > 300) //5 minutes resilienceTest test
		{
			break; 
		}
        }
        sqsClient.close();

    }
} 
