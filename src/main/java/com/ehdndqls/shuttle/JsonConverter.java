package com.ehdndqls.shuttle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

/*
 * JPA에서 List<Integer>를 DB의 문자열(String) 컬럼으로 저장하고 다시 역 질렬화해주는 커스텀 변환기
 * 실제 DB에는 JSON형태의 문자열로 저장되나 엔티티에선 List<Integer>로 자유롭게 사용할 수 있도록 해주는 역할
 */
@Converter
public class JsonConverter implements AttributeConverter<List<Integer>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert stops list to JSON string.", e);
        }
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to stops list.", e);
        }
    }
}
