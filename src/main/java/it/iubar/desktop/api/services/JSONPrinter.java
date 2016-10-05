package it.iubar.desktop.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONPrinter {

    public static <T> String toJson(T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.writeValueAsString(obj);
        
        // String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(staff1);
        
    }

}
