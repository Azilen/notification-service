package com.azilen.client.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Setter
public class ApplicationProperties {

    public static Properties loadProperties(String resourceFileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = ApplicationProperties.class
            .getClassLoader()
            .getResourceAsStream(resourceFileName);
        configuration.load(inputStream);
        inputStream.close();
        return configuration;
    }
    /*
    private pinPointConfig pinPointConfig = new pinPointConfig();
    private sqsConfig sqsConfig = new sqsConfig();

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class pinPointConfig{
        private String awsAccessKey;
        private String awsSecretAccessKey;
        private String awsRegion;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class sqsConfig{
        private message message = new message();
        private dl dl = new dl();
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class message{
        private queue queue = new queue();
        private String url;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class queue{
        String name;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @JsonSerialize
    public class dl{
       private queue queue = new queue();
       private String url;
       private String batchSize;
    }*/
}
