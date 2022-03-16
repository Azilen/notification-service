package com.azilen.client.producer;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.azilen.client.config.ApplicationProperties;
import com.azilen.common.vm.NotificationVM;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class NotificationReceiver {
    private AmazonSQS amazonSQS;
    private ReceiveMessageRequest receiveMessageRequest;
    private Gson gson = new Gson();
    private Properties applicationProperties;

    public void initializeConsumer() {
        try {
            applicationProperties = ApplicationProperties.loadProperties("application.yml");
            AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                applicationProperties.getProperty("awsAccessKey"), applicationProperties.getProperty("awsSecretAccessKey")));
            this.amazonSQS = AmazonSQSClientBuilder.standard().withCredentials(awsCredentialsProvider)
                .withRegion(Regions.fromName(applicationProperties.getProperty("awsRegion"))).build();
            receiveMessageRequest = new ReceiveMessageRequest(applicationProperties.getProperty("url"))
                .withMaxNumberOfMessages(Integer.parseInt(applicationProperties.getProperty("batchSize")));
        } catch (Exception e) {
            log.error("SQSConsumer not initialized", e);
        }
    }

    public List<NotificationVM> getUndeliveredNotifications() {
        List<NotificationVM> notifications = new ArrayList<>();
        final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();

        log.info("Processing undelivered messages::" + messages);
        for (Message messageObject : messages) {
            String messageBody = messageObject.getBody();

            NotificationVM notificationEvent = gson.fromJson(messageBody, NotificationVM.class);
            notifications.add(notificationEvent);
        }
        log.info("List of Unprocessed messages::" + messages);
        return notifications;
    }
}
