package it.iubar.desktop.api;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

public abstract class HttpClient {

	private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());
	private static final int DEF_CONNECT_TIMEOUT = 15000;
	private static final int DEF_READ_TIMEOUT = 15000;
	
	public static Client newClient() {
		ClientConfig configuration = new ClientConfig();
		configuration.property(ClientProperties.CONNECT_TIMEOUT, DEF_CONNECT_TIMEOUT);
		configuration.property(ClientProperties.READ_TIMEOUT, DEF_READ_TIMEOUT);
		Client client = ClientBuilder.newClient(configuration);
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
