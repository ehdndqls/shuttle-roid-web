package com.ehdndqls.shuttle.courses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class StopsConverter implements AttributeConverter<List<Long>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> stops) {
        try {
            return objectMapper.writeValueAsString(stops);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert stops list to JSON string.", e);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Long>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to stops list.", e);
        }
    }
}
