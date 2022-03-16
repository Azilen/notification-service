package com.azilen.common.vm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonSerialize
public class NotificationSubEventVM {

    @NotNull
    private String subEventType;

    @NotNull
    private String displayName;
}
