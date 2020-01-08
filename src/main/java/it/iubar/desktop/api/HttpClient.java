package it.iubar.desktop.api;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;

public abstract class HttpClient {

	private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());
	private static final int DEF_CONNECT_TIMEOUT = 1500;
	private static final int DEF_READ_TIMEOUT = 3000;
	
	public static Client newClient() {
		ClientConfig configuration = new ClientConfig();
		configuration.property(ClientProperties.CONNECT_TIMEOUT, DEF_CONNECT_TIMEOUT);
		configuration.property(ClientProperties.READ_TIMEOUT, DEF_READ_TIMEOUT);
		Client client = ClientBuilder.newClient(configuration);
		return client;
	}
	protected String url = null;

	public static JSONObject getAnswer(Response response) {
		JSONObject answer = null;
		if(response!=null) {
		String output = response.readEntity(String.class);
 
		try {
			answer = new JSONObject(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		return answer;
	}
 

}
