package ma.crm.carental.utils;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.persistence.AttributeConverter;

public class JsonConverter implements AttributeConverter<Map<String , Serializable> , String>{

    private final ObjectMapper objectMapper ;


    @Autowired
    JsonConverter(ObjectMapper objectMapper){
        this.objectMapper = objectMapper ;
    }


    @Override
    public String convertToDatabaseColumn(Map<String, Serializable> attribute) {
        if (attribute == null) {
            // Return an empty JSON object as a string
            return "{}"; 
        }
        
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON String to Map.", e);
        }
    }

    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Serializable> convertToEntityAttribute(String dbData) {
        
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (Exception e) {
            
            throw new RuntimeException("Error converting JSON String to Map.", e);
        }
    }
    
}
