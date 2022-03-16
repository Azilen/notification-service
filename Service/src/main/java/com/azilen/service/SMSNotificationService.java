package com.azilen.service;

import com.azilen.common.vm.NotificationParamVM;
import com.azilen.common.vm.NotificationVM;
import com.azilen.common.vm.SMSNotificationVM;
import com.azilen.config.ApplicationProperties;
import com.azilen.domain.NotificationHistory;
import com.azilen.domain.NotificationSubEvent;
import com.azilen.domain.NotificationTemplate;
import com.azilen.service.mapper.NotificationHistoryMapper;
import com.azilen.service.mapper.NotificationTemplateProcessor;
import com.azilen.web.rest.errors.BadRequestAlertException;
import com.azilen.web.rest.errors.SendNotificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class SMSNotificationService {
    private ApplicationProperties applicationProperties;
    private final String DEFAULT_FROM = "+17787672134";

    private NotificationHistoryMapper notificationHistoryMapper;

    private NotificationHistoryService notificationHistoryService;

    private NotificationTemplateService notificationTemplateService;

    @Autowired
    private NotificationSubEventService notificationSubEventService;

    SMSNotificationService(ApplicationProperties applicationProperties,
                           NotificationHistoryMapper notificationHistoryMapper,
                           NotificationHistoryService notificationHistoryService,
                           NotificationTemplateService notificationTemplateService) {

        this.applicationProperties = applicationProperties;
        this.notificationHistoryMapper = notificationHistoryMapper;
        this.notificationHistoryService = notificationHistoryService;
        this.notificationTemplateService = notificationTemplateService;

        Twilio.init(applicationProperties.getTwillioConfig().getSenderApiAccountSid(),
            applicationProperties.getTwillioConfig().getSenderApiAuthToken());
    }

    public void sendSMSNotification(NotificationVM notificationVM) throws SendNotificationException, TemplateException, IOException {

        NotificationSubEvent notificationSubEvent = notificationSubEventService.findBySubEventType(notificationVM.getSubNotificationEventType());

        ObjectMapper oMapper = new ObjectMapper();
        SMSNotificationVM smsNotificationVM = oMapper.convertValue(notificationVM.getNotificationVM(), SMSNotificationVM.class);

        List<String> toList = smsNotificationVM.getTo();
        for (String to : toList) {
            String smsBody = null;
            int version = 0;

            NotificationTemplate notificationTemplate = null;
            if (notificationVM.getTemplateData() != null && !notificationVM.getTemplateData().isEmpty()) {
                smsBody = notificationVM.getTemplateData();
                version = (notificationVM.getVersion() != null ? notificationVM.getVersion().intValue() : 1);
            }

            if (smsBody == null) {
                notificationTemplate = notificationTemplateService.findOneByEventTypeAndSubEventTypeAndIsGlobal(
                    notificationVM.getNotificationEventType(),
                    notificationVM.getSubNotificationEventType(),
                    true
                );
                if (notificationTemplate != null) {
                    smsBody = notificationTemplate.getContent();
                    version = notificationTemplate.getVersion();
                }
            }

            if (smsBody == null) {
                throw new BadRequestAlertException("No template data found to sent sms.", "smsManagement", "notFound");
            }

            String smsBodyContent = NotificationTemplateProcessor.getInstance().processTemplate(smsBody, version, notificationSubEvent.getDisplayName(), notificationVM.getHtmlVaraiblesData(), notificationVM.getNotificationEventType().getDisplayName());

            Message message = Message.creator(new PhoneNumber(to), // to
                new PhoneNumber(applicationProperties.getTwillioConfig().getSenderApiPhoneFrom()), // from
                smsBodyContent).create();

            NotificationHistory notificationHistory = notificationHistoryMapper.notificationVMtoNotificationHistory(notificationVM);
            notificationHistory.setContent(smsBodyContent);
            notificationHistory.setFromAddress(applicationProperties.getTwillioConfig().getSenderApiPhoneFrom());
            notificationHistory.setToAddress(to);

            if (CollectionUtils.isNotEmpty(notificationVM.getExtras())) {
                StringBuilder parameters = new StringBuilder();
                for (NotificationParamVM extraVm : notificationVM.getExtras()) {
                    parameters.append("__").append(extraVm.getKey()).append("__").append(extraVm.getValue());
                }

                notificationHistory.setParameters(parameters.toString());
            }

            notificationHistoryService.save(notificationHistory);
        }
    }
}
