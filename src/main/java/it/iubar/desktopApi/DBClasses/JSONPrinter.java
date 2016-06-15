package it.iubar.desktopApi.DBClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rits.cloning.Cloner;

public class JSONPrinter {

    public static <T> String toJson(T obj) throws JsonProcessingException, CloneNotSupportedException {
        ObjectMapper mapper = new ObjectMapper();
        Cloner cloner = new Cloner();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cloner.deepClone(obj));
    }

}
