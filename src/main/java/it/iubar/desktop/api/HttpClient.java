package it.iubar.desktop.api;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

 


public abstract class HttpClient {

	private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());
	private static final int DEF_CONNECT_TIMEOUT = 15000;
	private static final int DEF_READ_TIMEOUT = 15000;
	
	public static Client newClient() {
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, DEF_CONNECT_TIMEOUT);
		client.property(ClientProperties.READ_TIMEOUT, DEF_READ_TIMEOUT);		
		return client;
	}
	
	protected String url = null;

	public static JsonObject getAnswer(Response response) {
		JsonObject answer = null;
		if(response!=null) {
		String output = response.readEntity(String.class);
 		try {
			answer = it.iubar.desktop.api.json.JsonUtils.parseJsonString(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		return answer;
	}

	
 
}
