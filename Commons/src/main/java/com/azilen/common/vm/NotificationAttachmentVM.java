package com.azilen.common.vm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonSerialize
public class NotificationAttachmentVM {
    private String fileName;
    private String url;
    private String base64;
}
