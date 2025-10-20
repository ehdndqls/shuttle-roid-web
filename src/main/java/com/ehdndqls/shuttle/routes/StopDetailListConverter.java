package com.ehdndqls.shuttle.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class StopDetailListConverter implements AttributeConverter<List<StopDetail>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<StopDetail> stopDetails) {
        try {
            return objectMapper.writeValueAsString(stopDetails);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting StopDetail list to JSON", e);
        }
    }

    @Override
    public List<StopDetail> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<StopDetail>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error reading StopDetail list from JSON", e);
        }
    }
}
