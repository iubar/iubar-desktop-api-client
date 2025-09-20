package it.iubar.desktop.api;

import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;

public abstract class AuthHttpClient extends HttpClientUtils {

	private static final Logger LOGGER = Logger.getLogger(AuthHttpClient.class.getName());


	private String user = null;
	private String apiKey = null;
	private boolean isAuth = false;
	
	 
	abstract protected JsonObject genAuth2(String destUrl);
		
	protected String getUser() {
		return this.user;
	}

	public AuthHttpClient() {
		super();
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	protected String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	protected boolean isAuth() {
		return this.isAuth;
	}

	public void setAuth(boolean auth) {
		this.isAuth = auth;
	}
	
	private JsonObject genAuth(String destUrl, JsonObject jsonObj) {
		JsonObject authData = genAuth2(destUrl);		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder(authData);
		if (jsonObj != null) {
			objectBuilder.add("data", jsonObj);
		}
		
		return objectBuilder.build();
	}

	private JsonObject genAuth(String destUrl, JsonArray jsonArray) {
		JsonObject authData = genAuth2(destUrl);		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder(authData);
		if (jsonArray != null) {
			objectBuilder.add("data", jsonArray);
		}
		
		return objectBuilder.build();
	}

	protected JsonObject genAuth(String destUrl) {
		JsonObject authData = genAuth2(destUrl);
		return authData;
	}
	
	public Response doPost(String restUrl, JsonObject data) {
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			data = genAuth(restUrl, data);
		}
		return super.doPost(restUrl, data);
	}	

}
