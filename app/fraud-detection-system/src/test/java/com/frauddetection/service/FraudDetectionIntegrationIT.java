package com.frauddetection.service;

import org.junit.jupiter.api.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.net.URI;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FraudDetectionIntegrationIT {

    private SqsClient sqsClient;
    private CloudWatchLogsClient logsClient;
    private String queueUrl;
    private final String queueName = "fraud-test-queue";
    private final String logGroupName = "/aws/fraud/test/logs";
    private final String logStreamName = "test-stream";

    @BeforeAll
    void setup() {
        var creds = StaticCredentialsProvider.create(
                AwsBasicCredentials.create("test", "test")
        );

        sqsClient = SqsClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .region(Region.of("us-east-1"))
                .credentialsProvider(creds)
                .build();

        logsClient = CloudWatchLogsClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .region(Region.of("us-east-1"))
                .credentialsProvider(creds)
                .build();

        CreateQueueResponse createResult = sqsClient.createQueue(CreateQueueRequest.builder()
                .queueName(queueName)
                .build());
        queueUrl = createResult.queueUrl();

        // Create log group and stream
        logsClient.createLogGroup(CreateLogGroupRequest.builder()
                .logGroupName(logGroupName)
                .build());
        logsClient.createLogStream(CreateLogStreamRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .build());
    }

    @Test       // 2.1 integration test: to test the message is sent and received from Localstack SQS queue
    void testSendAndReceiveMessage() {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody("{ \"transaction_id\": \"it-test-001\", \"amount\": 9999 }")
                .build());

        ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(5)
                .maxNumberOfMessages(1)
                .build());

        List<Message> messages = response.messages();
        Assertions.assertFalse(messages.isEmpty(), "No message received from test queue");
        System.out.println("Received: " + messages.get(0).body());
    }

    @Test       // 2.2 integration test: to test the log is written to Localstack CloudWatch Logs
    void testCloudWatchLogging() {
        PutLogEventsResponse logResponse = logsClient.putLogEvents(PutLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .logEvents(InputLogEvent.builder()
                        .message("Fraud detection test log")
                        .timestamp(System.currentTimeMillis())
                        .build())
                .build());

        Assertions.assertNotNull(logResponse, "CloudWatch log event response is null");
        System.out.println("Log event written to CloudWatch Logs. it-test-002");
    }

    @AfterAll
    void cleanup() {
        sqsClient.deleteQueue(DeleteQueueRequest.builder()
                .queueUrl(queueUrl)
                .build());
        logsClient.deleteLogStream(DeleteLogStreamRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .build());
        logsClient.deleteLogGroup(DeleteLogGroupRequest.builder()
                .logGroupName(logGroupName)
                .build());

        sqsClient.close();
        logsClient.close();
    }
}
