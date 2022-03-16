package com.azilen.common.dto;

import com.azilen.common.enums.NotificationEventType;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.ALWAYS)
public class NotificationTemplateDTO extends BaseDTO {
    private String content;
    private NotificationEventType eventType;
    private String subEventType;
    private int version;
    private Boolean isGlobal;
}
