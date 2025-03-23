package com.frauddetection.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlertConsumerClient {

    private static final String QUEUE_NAME = "fraud-alert-queue"; // change if needed
    private static final String REGION = "ap-southeast-2"; // your region
    private static final String ENDPOINT = "https://sqs.ap-southeast-2.amazonaws.com"; // your SQS endpoint
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

        System.out.println("[Consumer] Listening for fraud alerts...");

        int counter = 1;
        while (true) {
            ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .waitTimeSeconds(10)
                    .maxNumberOfMessages(1)
                    .build());

            List<Message> messages = response.messages();
            for (Message message : messages) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.printf("[Consumer] Received alert at %s: %s\n", timestamp, message.body());

                // delete the message from the queue
                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build());
            }
            counter++;
            if(counter > 300 ) //5 minutes resilienceTest test
            {
                break; 
            }
        }
        sqsClient.close();
    }
}
