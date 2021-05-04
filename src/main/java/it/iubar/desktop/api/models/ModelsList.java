package it.iubar.desktop.api.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import it.iubar.desktop.api.json.JsonUtils;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
 

public class ModelsList<T extends IJsonModel> {
	
	private final static Logger LOGGER = Logger.getLogger(ModelsList.class.getName());
	
	String mac = null;
	String jsonName = null;
	List<T> list = new ArrayList<T>();

	private int idapp;
	
	public Class getElemClass(){
		T t = this.list.get(0);
		return t.getClass();
	};
	
	public String getJsonName(){
		return this.jsonName;
	};
	
	public ModelsList(String mac, int idapp, String jsonName) {
		this.mac = mac;
		this.idapp = idapp;
		this.jsonName = jsonName;
	}
 
	public void add(T model){
		this.list.add(model);
	}

	public int getSize(){
		int n = 0;
		if(this.list!=null){
			n = this.list.size();
		}
		return n;
	};
	
	public JsonArray getJsonArray()   {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		if(this.list!=null){
			for (T t : this.list) {
				String json = t.asJson();
				JsonObject jsonObj = JsonUtils.fromString(json);
				arrayBuilder.add(jsonObj);
			}
		}
		return arrayBuilder.build();
	}
 

	public String getMac() {
		return this.mac;
	}

	public int getIdApp() {
		return this.idapp;
	}
	
}
