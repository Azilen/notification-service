package com.azilen.consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.azilen.api.EmailNotificationApiService;
import com.azilen.api.SMSNotificationApiService;
import com.azilen.common.vm.NotificationVM;
import com.azilen.config.ApplicationProperties;
import com.azilen.processor.NotificationProcessor;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class SQSConsumer implements Runnable{
    private ApplicationProperties applicationProperties;

    private AmazonSQS amazonSQS;

    private ReceiveMessageRequest receiveMessageRequest;
    private boolean running = false;
    private ExecutorService executor = null; // batch size = core
    private ScheduledExecutorService schExecutor = null;
    private ConcurrentHashMap<String, Message> notificationMessage = new ConcurrentHashMap<String, Message>();
    private EmailNotificationApiService emailNotificationApiService;
    private SMSNotificationApiService smsNotificationApiService;

    @Autowired
    public SQSConsumer(ApplicationProperties applicationProperties,EmailNotificationApiService emailNotificationApiService,SMSNotificationApiService smsNotificationApiService) {
        try {
            this.applicationProperties = applicationProperties;

            AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(this.applicationProperties.getAwsConfig().getAccessKey(),
                    this.applicationProperties.getAwsConfig().getSecretKey()));
            this.amazonSQS = AmazonSQSClientBuilder.standard().withCredentials(awsCredentialsProvider)
                .withRegion(Regions.fromName(this.applicationProperties.getAwsConfig().getRegion())).build();
            this.receiveMessageRequest = new ReceiveMessageRequest(this.applicationProperties.getSqsConsumerConfig().getUrl())
                .withMaxNumberOfMessages(Integer.parseInt(this.applicationProperties.getSqsConsumerConfig().getBatchSize()))
                .withWaitTimeSeconds(Integer.parseInt(this.applicationProperties.getSqsConsumerConfig().getWaitTime()));
            this.emailNotificationApiService = emailNotificationApiService;
            this.smsNotificationApiService = smsNotificationApiService;
        } catch (Exception e) {
            log.error("SQSConsumer not initialized", e);
        }
    }

    @PostConstruct
    public void initialize() {
        log.info("Staring SQS Consumer");
        start();
        log.info("Started SQS Consumer");

    }

    public synchronized void start() {
        if (!running) {
            executor = Executors.newFixedThreadPool(10);
            schExecutor = Executors.newScheduledThreadPool(1);
            schExecutor.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
            running = true;
        }
    }

    public void deleteMessage(String messageReceiptHandle) {
        amazonSQS.deleteMessage(new DeleteMessageRequest(this.applicationProperties.getSqsConsumerConfig().getUrl(), messageReceiptHandle));
    }

    public synchronized void stop() {
        running = false;
        executor.shutdown();
        schExecutor.shutdown();
        executor = null;
        schExecutor = null;
    }

    @PreDestroy
    public void destroy() {
        stop();
    }

    public synchronized boolean status() {
        // TODO Auto-generated method stub
        return running;
    }

    @Override
    public void run() {
        while (running) {
            try {
                final List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
                if (!messages.isEmpty()) {
                    Collection<Callable<String>> callables = processMessages(messages);
                    if (!callables.isEmpty()) {
                        List<Future<String>> results = executor.invokeAll(callables);
                        for (Future<String> result : results) {
                            if (result.get() != null) {
                                log.info("Deleting message::" + result.get());
                                Message message = notificationMessage.get(result.get());
                                deleteMessage(message.getReceiptHandle());
                                notificationMessage.remove(result.get());
                                log.info("Deleted message::" + result.get());
                            } else {
                                log.error("Did not get the notification id");
                            }
                        }
                    }
                }
            } catch (Exception e1) {
                log.error("While processing notification exception occured::{}", e1);
            }

        }

    }

    private Collection<Callable<String>> processMessages(List<Message> messages) {
        log.info("Processing messages::" + messages);
        Collection<Callable<String>> callables = new ArrayList<>();
        for (Message messageObject : messages) {
            String messageBody = messageObject.getBody();

            Gson gson = new Gson();
            NotificationVM notificationVM = gson.fromJson(messageBody, NotificationVM.class);

            notificationMessage.put(notificationVM.getNotificationId(), messageObject);
            log.debug("Notification received::{}", notificationVM);

            NotificationProcessor sqsNotificationProcessor = new NotificationProcessor(notificationVM,emailNotificationApiService,smsNotificationApiService);
            callables.add(sqsNotificationProcessor);
        }
        log.info("Processed messages::" + messages);
        return callables;
    }
}
