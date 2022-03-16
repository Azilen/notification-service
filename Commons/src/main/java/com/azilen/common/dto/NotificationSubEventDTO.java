package com.azilen.common.dto;

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
public class NotificationSubEventDTO extends BaseDTO {
    private String subEventType;
    private String displayName;
}
