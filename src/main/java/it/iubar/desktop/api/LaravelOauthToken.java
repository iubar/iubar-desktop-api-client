package it.iubar.desktop.api;

import it.iubar.desktop.api.json.JsonUtils;
import jakarta.json.JsonObject;

public class LaravelOauthToken {
	
	private String type = null;
	private String accessToken = null;
	private String refreshToken = null;
	
	public LaravelOauthToken(String data) {	 
		JsonObject jsonData = JsonUtils.parseJsonString(data);		
		this.type = jsonData.getString("token_type");
		this.accessToken = jsonData.getString("access_token");
		this.refreshToken = jsonData.getString("refresh_token");		
		// TODO
		// The expires_in attribute contains the number of seconds until the access token expires.
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public String getRefreshToken() {
		return this.refreshToken;
	}
	
}