package com.azilen.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Azilen Notification Service.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private pinPointConfig pinPointConfig = new pinPointConfig();
    private twillioConfig twillioConfig = new twillioConfig();
    private sqsConsumerConfig sqsConsumerConfig = new sqsConsumerConfig();
    private awsConfig awsConfig = new awsConfig();

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class pinPointConfig {
        private String projectId;
        private String projectName;
        private String awsAccessKey;
        private String awsSecretAccessKey;
        private String awsRegion;
        private String emailSender;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class awsConfig {
        private String accessKey;
        private String secretKey;
        private String region;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class twillioConfig {
        private String verificationApiKey;
        private String senderApiAccountSid;
        private String senderApiAuthToken;
        private String senderApiPhoneFrom;

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class sqsConsumerConfig {
        private String queuename;
        private String url;
        private String batchSize;
        private String waitTime;
    }

}
