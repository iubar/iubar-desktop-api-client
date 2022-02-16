package it.iubar.desktop.api;

import java.util.logging.Logger;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
 

public class LaravelClient extends AuthHttpClient  {

	private final static Logger LOGGER = Logger.getLogger(LaravelClient.class.getName());

	private String username = null;
	private String password = null;
	private String loginUrl = null;
	
	private int clientId = 0;
	private String clientSecret = null;
	
	private LaravelOauthToken token = null;
	
	public LaravelClient() {
		super();
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * Autenticazione Oauth2 con password grant
	 * @todo: Daniele scrivere test
	 * @param email
	 * @param password
	 * @return
	 */
	protected JsonObject getAuthParams() {		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		objectBuilder.add("grant_type", "password");
		objectBuilder.add("client_id", this.clientId);
		objectBuilder.add("client_secret", this.clientSecret);
		// [previous exception] [object] (League\\OAuth2\\Server\\Exception\\OAuthServerException(code: 4): Client authentication failed at " + System.getProperty("user.home") + "\\workspace_php\\hr-laravel\\vendor\\league\\oauth2-server\\src\\Exception\\OAuthServerException.php:154)
		// objectBuilder.add("client_id", 2);
		// objectBuilder.add("client_secret", this.clientSecret);
		objectBuilder.add("username", this.username);
		objectBuilder.add("password", this.password);
		objectBuilder.add("scope", ""); // oppure  'scope' => '*',						
		JsonObject authData = objectBuilder.build();		
		return authData;
	}

	public LaravelOauthToken getToken(){
		if (this.token == null) {
			Response response = post(this.loginUrl, getAuthParams());
			if (response != null) {
				int status = response.getStatus();
				if (status == Status.CREATED.getStatusCode() || status == Status.OK.getStatusCode()) {
					String output = response.readEntity(String.class);
					this.token = new LaravelOauthToken(output);
				}
			}
		}
		
		return this.token;
	}


	@Override
	protected JsonObject genAuth2(String destUrl) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response get(String restUrl) {
		Response response = null;
		try {
			restUrl = resolveUrl(restUrl);
			
			LOGGER.info("GET:" + restUrl);
			
			Client client = HttpClient.newClient();
			WebTarget target = client.target(restUrl); // il metodo codifica il parametro (la stringa che rappresenta l'url) in modo analogo alla funzione PHP rawurlencode()
			LaravelOauthToken token = getToken();
						
			response = target.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token.getAccessToken())
				.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public Response postData(String restUrl, Entity<String> data) {
		Response response = null;
		try {
			restUrl = resolveUrl(restUrl);
			
			LOGGER.info("POST:" + restUrl);
			
			Client client = HttpClient.newClient();
			WebTarget target = client.target(restUrl); // il metodo codifica il parametro (la stringa che rappresenta l'url) in modo analogo alla funzione PHP rawurlencode()
			LaravelOauthToken token = getToken();
						
			response = target.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				//.header("X-Requested-With", "XMLHttpRequest")
				.header("Authorization", "Bearer " + token.getAccessToken())
				.post(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}

}