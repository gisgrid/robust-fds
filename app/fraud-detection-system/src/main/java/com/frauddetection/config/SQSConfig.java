package com.frauddetection.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SQSConfig {

    private static final Logger logger = LoggerFactory.getLogger(SQSConfig.class);

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.sqs.transaction-queue-url}")
    private String transactionQueueUrl;

    @Value("${aws.sqs.fraud-alert-queue-url}")
    private String fraudAlertQueueUrl;

    @Bean
    public SqsClient sqsClient() {
        try {
            Region region = Region.of(awsRegion);
            logger.info("Initializing SQS client for region: {}", awsRegion);
            logger.info("Transaction Queue URL: {}", transactionQueueUrl);
            logger.info("Fraud Alert Queue URL: {}", fraudAlertQueueUrl);

            return SqsClient.builder()
                    .region(region)
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error("Invalid AWS region: {}. Please check your configuration.", awsRegion, e);
            throw e;
        }
    }

    @Bean
    public String transactionQueueUrl() {
        return transactionQueueUrl;
    }

    @Bean
    public String fraudAlertQueueUrl() {
        return fraudAlertQueueUrl;
    }
} 


/*未来改进点
 * 1、对 aws.region 的校验
 * 2、缺少日志记录：
 * 3、缺少异常处理：
 * 
 参考
 package com.frauddetection.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SQSConfig {

    private static final Logger logger = LoggerFactory.getLogger(SQSConfig.class);

    @Value("${aws.region:us-east-1}") // 默认值为 us-east-1
    private String awsRegion;

    @Bean
    public SqsClient sqsClient() {
        try {
            // 构建 SQS 客户端
            Region region = Region.of(awsRegion); // 如果 awsRegion 无效，会抛出 IllegalArgumentException
            logger.info("Initializing SQS client for region: {}", awsRegion);

            return SqsClient.builder()
                    .region(region)
                    .credentialsProvider(DefaultCredentialsProvider.create()) // 使用默认凭证提供者
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error("Invalid AWS region: {}. Please check your configuration.", awsRegion, e);
            throw e; // 抛出异常，停止应用启动
        }
    }
}

 */