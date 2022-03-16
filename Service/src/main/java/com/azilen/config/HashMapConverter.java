package com.azilen.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class HashMapConverter implements AttributeConverter<Map<String, String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> info) {

        String infoJson = null;
        try {
            infoJson = objectMapper.writeValueAsString(info);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return infoJson;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String infoJSON) {

        Map<String, String> info = null;
        try {
            info = objectMapper.readValue(infoJSON, Map.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return info;
    }

}
