package com.azilen.common.vm;

import com.azilen.common.enums.NotificationEventType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonSerialize
public class NotificationVM {
    private String notificationId;
    private NotificationEventType notificationEventType;
    private String subNotificationEventType;
    private String templateData;
    private Map<String, Object> htmlVaraiblesData;
    private Object notificationVM;
    private Long version;
    private Set<NotificationParamVM> extras;
    private Set<NotificationAttachmentVM> attachments;
}
