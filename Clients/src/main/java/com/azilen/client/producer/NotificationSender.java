package com.azilen.client.producer;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.azilen.client.config.ApplicationProperties;
import com.azilen.client.constant.Constants;
import com.azilen.common.vm.NotificationVM;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class NotificationSender {
    private Properties applicationProperties;

    private AmazonSQS amazonSQS;

    public void initializeProducer() {
        try {
            applicationProperties = ApplicationProperties.loadProperties("application.yml");

            AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                applicationProperties.getProperty("awsAccessKey"), applicationProperties.getProperty("awsSecretAccessKey")));

            this.amazonSQS = AmazonSQSClientBuilder.standard().withCredentials(awsCredentialsProvider)
                .withRegion(Regions.fromName(applicationProperties.getProperty("awsRegion"))).build();
        } catch (Exception e) {
            log.error("SQSConsumer not initialized", e);
        }
    }

    public void send(NotificationVM notificationVM, String profile) {
        try {
            switch(profile) {
                case Constants.SPRING_PROFILE_DEVELOPMENT :
                    this.amazonSQS.sendMessage(applicationProperties.getProperty("devUrl"), new Gson().toJson(notificationVM));
                    break;
                case Constants.SPRING_PROFILE_PRODUCTION:
                    this.amazonSQS.sendMessage(applicationProperties.getProperty("prodUrl"), new Gson().toJson(notificationVM));
                    break;
                case Constants.SPRING_PROFILE_UAT:
                    this.amazonSQS.sendMessage(applicationProperties.getProperty("uatUrl"), new Gson().toJson(notificationVM));
                    break;
            }

        } catch (Exception e) {
            log.error("could not send notification ::notification={}", e);
        }
    }
}
