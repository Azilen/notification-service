package com.azilen.api;

import com.azilen.common.vm.NotificationVM;
import com.azilen.service.SMSNotificationService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SMSNotificationApiService {
    @Autowired
    SMSNotificationService smsNotificationService;

    public void sendSMSNotification(NotificationVM notificationVM) throws TemplateException, IOException {
        smsNotificationService.sendSMSNotification(notificationVM);
    }
}
