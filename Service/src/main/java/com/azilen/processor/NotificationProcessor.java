package com.azilen.processor;

import com.azilen.api.EmailNotificationApiService;
import com.azilen.api.SMSNotificationApiService;
import com.azilen.common.enums.NotificationEventType;
import com.azilen.common.vm.NotificationVM;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class NotificationProcessor implements Callable<String> {
    private NotificationVM notificationVM;
    private EmailNotificationApiService emailNotificationApiService;
    private SMSNotificationApiService smsNotificationApiService;

    public NotificationProcessor(NotificationVM notificationVM, EmailNotificationApiService emailNotificationApiService, SMSNotificationApiService smsNotificationApiService) {
        this.notificationVM = notificationVM;
        this.emailNotificationApiService = emailNotificationApiService;
        this.smsNotificationApiService = smsNotificationApiService;
    }

    @Override
    public String call() {
        log.info("Processing Notification Event in SQSNotificatioProcessor:: ID={}"+this.notificationVM.getNotificationId());
        try {
            NotificationEventType eventType = this.notificationVM.getNotificationEventType();
            log.info("Event Type :"+ eventType);
            switch (eventType) {
                case EMAIL:
                    emailNotificationApiService.sendEmailNotification(this.notificationVM);
                    return this.notificationVM.getNotificationId();
                case SMS:
                    smsNotificationApiService.sendSMSNotification(this.notificationVM);
                    return this.notificationVM.getNotificationId();
            }
        } catch (Exception ex) {
            log.error("Exception in processing Notification Event {}",ex.getMessage(),ex);
            return null;
        }
        return null;
    }
}
