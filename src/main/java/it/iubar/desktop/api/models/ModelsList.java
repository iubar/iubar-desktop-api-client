package it.iubar.desktop.api.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import it.iubar.desktop.api.json.JsonUtils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.bind.annotation.JsonbTransient;

public class ModelsList<T extends IJsonModel> {

    private static final Logger LOGGER = Logger.getLogger(ModelsList.class.getName());

    private String mac;
    private String jsonName;
    private List<T> list = new ArrayList<>();
    private int idapp;

    public ModelsList() {
    }

    public ModelsList(String mac, int idapp, String jsonName) {
        this.mac = mac;
        this.idapp = idapp;
        this.jsonName = jsonName;
    }

    public String getJsonName() {
        return jsonName;
    }

    public String getMac() {
        return mac;
    }

    public int getIdApp() {
        return idapp;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = (list != null) ? list : new ArrayList<>();
    }

    @JsonbTransient
    public Class<?> getElemClass() {
        if (this.list == null || this.list.isEmpty()) {
            return null;
        }
        return this.list.get(0).getClass();
    }

    public void add(T model) {
        if (this.list == null) {
            list = new ArrayList<>();
        }
        this.list.add(model);
    }

    @JsonbTransient
    public int getSize() {
        return list == null ? 0 : this.list.size();
    }

    @JsonbTransient
    public JsonArray getJsonArray() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        if (this.list != null) {
            for (T t : this.list) {
                if (t != null) {
                    String json = t.asJson();
                    JsonObject jsonObj = JsonUtils.fromString(json);
                    arrayBuilder.add(jsonObj);
                }
            }
        }

        return arrayBuilder.build();
    }
}

