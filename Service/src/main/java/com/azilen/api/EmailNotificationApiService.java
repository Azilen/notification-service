package com.azilen.api;

import com.azilen.common.vm.NotificationVM;
import com.azilen.service.EmailNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationApiService {
    @Autowired
    EmailNotificationService emailNotificationService;

    public void sendEmailNotification(NotificationVM notificationVM) {
        emailNotificationService.sendEmailNotifiation(notificationVM);
    }
}
