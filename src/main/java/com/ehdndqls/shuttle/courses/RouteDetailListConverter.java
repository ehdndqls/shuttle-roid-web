package com.ehdndqls.shuttle.courses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class RouteDetailListConverter implements AttributeConverter<List<RouteDetail>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // LocalTime 등 Java 8 date/time 지원
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final TypeReference<List<RouteDetail>> typeRef = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<RouteDetail> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert RouteDetail list to JSON string.", e);
        }
    }

    @Override
    public List<RouteDetail> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to RouteDetail list.", e);
        }
    }
}
