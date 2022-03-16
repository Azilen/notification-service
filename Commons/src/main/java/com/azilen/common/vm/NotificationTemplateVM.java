package com.azilen.common.vm;

import com.azilen.common.enums.NotificationEventType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonSerialize
public class NotificationTemplateVM {
    private String content;
    private NotificationEventType eventType;
    private String subEventType;
    private Boolean isGlobal;
}
