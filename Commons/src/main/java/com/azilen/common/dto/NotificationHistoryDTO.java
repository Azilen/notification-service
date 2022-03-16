package com.azilen.common.dto;

import com.azilen.common.enums.NotificationEventType;
import com.azilen.common.vm.NotificationAttachmentVM;
import com.azilen.common.vm.NotificationParamVM;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.ALWAYS)
public class NotificationHistoryDTO extends BaseDTO {
    private String notificationId;
    private NotificationEventType eventType;
    private String subEventType;
    private String content;
    private String fromAddress;
    private String toAddress;
    private Set<NotificationParamVM> extras;
    private Set<NotificationAttachmentVM> attachments;
}
