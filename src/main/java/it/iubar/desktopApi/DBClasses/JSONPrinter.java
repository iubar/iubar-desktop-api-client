package it.iubar.desktopApi.DBClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONPrinter {

    public static <T> String toJson(T obj) throws JsonProcessingException, CloneNotSupportedException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

}
