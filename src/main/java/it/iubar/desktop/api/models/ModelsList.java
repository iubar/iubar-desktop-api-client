package it.iubar.desktop.api.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

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
	
	public JSONArray getJsonArray() throws JSONException, JsonProcessingException {
		JSONArray jsonArray = new JSONArray();
		if(this.list!=null){
			for (T t : this.list) {
				String json = t.asJson();
				JSONObject jsonObj = new JSONObject(json);
				jsonArray.put(jsonObj);
			}
		}
		return jsonArray;
	}
 
    
//	public String getJson() {
//		String str = "";
//		try {
//			JSONArray jsonArray = getJsonArray();
//			str = jsonArray.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {		
//			e.printStackTrace();
//            LOGGER.log(Level.SEVERE, "Jackson could not convert the object correctly.", JsonProcessingException.class);		
//		}
//		return str;
//	}

	public String getMac() {
		return this.mac;
	}

	public Object getIdApp() {
		return this.idapp;
	}
	
}
