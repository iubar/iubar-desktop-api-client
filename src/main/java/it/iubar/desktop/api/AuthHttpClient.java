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
	
	public Response post(String restUrl, JsonObject data) {
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			data = genAuth(restUrl, data);
		}
//		Entity<String> d1 = Entity.text(data.toString());
//		Entity<String> d2 = Entity.entity(data.toString(), MediaType.APPLICATION_JSON);
		Entity<String> d3 = Entity.json(data.toString()); // See:
															// https://jersey.java.net/documentation/latest/client.html#d0e4692
		// Se volessi utilizzare il post di tipo "application/x-www-form-urlencoded"
		// Form form = new Form();
		// form.param("user", "XXX");
		// Entity d4 = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED);

		return post(restUrl, d3);
	}	

}
