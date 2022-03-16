package com.azilen.web.rest.vm;

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
public class PinPointClientConfiguration {
    private String projectId;
    private String projectName;
    private String emailSender;
    private String awsAccessKey;
    private String awsSecretAccessKey;
    private String awsRegion;
}

