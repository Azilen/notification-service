package com.azilen.common.vm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonSerialize
public class NotificationParamVM {
    private String key;
    private String value;
}
