package com.frauddetection.config;

import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import io.micrometer.cloudwatch2.CloudWatchConfig;
import java.time.Duration;

@Configuration
public class CloudWatchMetricsConfig {

    @Value("${aws.region}")
    private String awsRegion;

    //cloudWatchAsyncClient: Creates an async CloudWatch client with the configured region
    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    //cloudWatchMeterRegistry: Creates a CloudWatchMeterRegistry bean that integrates Micrometer metrics with AWS CloudWatch
    //Custom namespace "FraudDetection"
    //60-second step for metrics collection
    //20-batch size for metrics collection  
    //enabled: true

    @Bean
    public MeterRegistry cloudWatchMeterRegistry(CloudWatchAsyncClient cloudWatchAsyncClient) {
        CloudWatchConfig config = new CloudWatchConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String namespace() {
                return "FraudDetection";
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(60);
            }

            @Override
            public boolean enabled() {
                return true;
            }

            @Override
            public int batchSize() {
                return 20;
            }
        };

        return new CloudWatchMeterRegistry(config, Clock.SYSTEM, cloudWatchAsyncClient);
    }
} 