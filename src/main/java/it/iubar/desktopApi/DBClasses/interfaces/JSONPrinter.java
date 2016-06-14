package it.iubar.desktopApi.DBClasses.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JSONPrinter {
    public String toJson() throws JsonProcessingException;
}
