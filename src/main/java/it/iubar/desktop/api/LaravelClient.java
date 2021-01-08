package it.iubar.desktop.api;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

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
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	protected JSONObject getAuthParams() {
		JSONObject authData = new JSONObject();
		authData.put("grant_type", "password");
		authData.put("client_id", this.clientId);
		authData.put("client_secret", this.clientSecret);
		// [previous exception] [object] (League\\OAuth2\\Server\\Exception\\OAuthServerException(code: 4): Client authentication failed at " + System.getProperty("user.home") + "\\workspace_php\\hr-laravel\\vendor\\league\\oauth2-server\\src\\Exception\\OAuthServerException.php:154)
		// authData.put("client_id", 2);
		// authData.put("client_secret", this.clientSecret);
		authData.put("username", this.username);
		authData.put("password", this.password);
		authData.put("scope", ""); // oppure  'scope' => '*',
		
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
	protected JSONObject genAuth2(String destUrl) {
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
				.accept("application/json")
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
				.accept("application/json")
				.header("X-Requested-With", "XMLHttpRequest")
				.header("Authorization", "Bearer " + token.getAccessToken())
				.post(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}

}