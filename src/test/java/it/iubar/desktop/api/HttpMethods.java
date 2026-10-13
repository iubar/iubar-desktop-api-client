package it.iubar.desktop.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.jupiter.api.BeforeAll;

import it.iubar.desktop.api.json.JsonUtils;
import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;
import it.iubar.desktop.api.utils.QueryStringParser;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

public class HttpMethods {
	
	protected HttpClient2025 masterClient = null;
	
	private JsonObject jsonObject = null;
	
	private static final Logger LOGGER = Logger.getLogger(HttpMethods.class.getName());
 
	public HttpMethods(String baseUrl) {

		this.masterClient = new HttpClient2025();		
		// masterClient.loadConfig();
		this.masterClient.setBaseUrl(baseUrl);
 
	}
	
	public HttpClient2025 getClient() {
		return this.masterClient;
	}

	public void modelSend(String url, IJsonModel model) {
	
		//masterClient.setAuth(false);
		JsonObject jsonObjModel = null;
		try {
			if (url != null) {
				jsonObjModel = masterClient.send(url, model);
			} else {
				jsonObjModel = masterClient.send(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.getMessage());
		}
		int count = 0;
		assertNotNull(jsonObjModel);		
		if(jsonObjModel!=null) {
			count = jsonObjModel.getInt("data");
		}
		assertTrue(count >= 0);

	}

	public  void modlesSend(ModelsList models) throws Exception {
	 
 		JsonObject jsonObjModel = null;
 
			jsonObjModel = masterClient.send(models);
			int count = jsonObjModel.getInt("data");
			assertNotNull(jsonObjModel);
			assertTrue(count >= 0);
 
	}

	public void send(String input, String path, boolean request) {
		Client client = HttpClientAbstract.newClient();
		String baseUrlRaw = this.masterClient.getBaseUrl();
		URI baseUri = UriBuilder.fromUri(baseUrlRaw).build();
		WebTarget target = client.target(baseUri);

		LOGGER.info("Testing path \"" + baseUri.toString() + path + "\" ...");
		Response response = target.path(path).request().accept(MediaType.APPLICATION_JSON).post(Entity.json(input));
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		this.jsonObject = JsonUtils.fromString(json);
		LOGGER.info("...request: " + input + " | response: " + this.jsonObject.toString() + "\n");

		int count = 0;

		if (request) {
			count = this.jsonObject.getInt("data");
			isOk(statusCode, count);
		} else {
			JsonObject error = this.jsonObject.getJsonObject("error");
			isBadRequest(error.getString("description"), statusCode);
		}
	}

	public void isBadRequest(String message, int statusCode) {
		assertNotNull(message);
		LOGGER.info("Message: " + message);
		String txt = "Status: " + statusCode ;;
		LOGGER.info(txt);
		assertEquals(Status.BAD_REQUEST.getStatusCode(), statusCode);
		assertEquals(Status.BAD_REQUEST.getStatusCode(), this.jsonObject.getInt("code"));
	}

	public void isOk(int statusCode, int count) {
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertTrue(count >= 0);
	 
	}

	public void isDataFalse() {
		boolean data = this.jsonObject.getBoolean("data");
		assertFalse(data);
	}

	public  void receive(String path) {
		// Client client = HttpClientAbstract.newClient();
		dummy(this.masterClient.getClient(), path, null);
	}

	public  void receive(String path, Map<String, String> queryParams) {
		// Client client = HttpClientAbstract.newClient();
		dummy(this.masterClient.getClient(), path, queryParams);
	}

	public  void receiveProtected(String path) {
		// Client client = HttpClientAbstract.newClientProtected(MasterClientAbstract.CRM_HTTP_USER, MasterClientAbstract.CRM_HTTP_PASSWORD);
		Client client = this.masterClient.getClient();
		client.register(HttpAuthenticationFeature.basic(MasterClientAbstract.CRM_HTTP_USER, MasterClientAbstract.CRM_HTTP_PASSWORD));
		dummy(client, path, null);
	}

	public void receiveProtected(String path, Map<String, String> queryParams) {
		// Client client = HttpClientAbstract.newClientProtected(MasterClientAbstract.CRM_HTTP_USER, MasterClientAbstract.CRM_HTTP_PASSWORD);
		Client client = this.masterClient.getClient();
		client.register(HttpAuthenticationFeature.basic(MasterClientAbstract.CRM_HTTP_USER, MasterClientAbstract.CRM_HTTP_PASSWORD));		
		dummy(client, path, queryParams);
	}	

	private void dummy(Client client, String path, Map<String, String> queryParams) {
		int statusCode = 0;
		String json = null;
		String baseUrlRaw = this.masterClient.getBaseUrl();
		URI baseUri = UriBuilder.fromUri(baseUrlRaw).build();
		WebTarget target = client.target(baseUri).path(path);

		if(queryParams!=null && !queryParams.isEmpty()) {
			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				target = target.queryParam(entry.getKey(), entry.getValue());
			}
		}

		LOGGER.info("Testing route \"" + target.toString() +   "\" ...");

		Response response = target.request().accept(MediaType.APPLICATION_JSON).get(Response.class);		
		statusCode = response.getStatus();
		json = response.readEntity(String.class);				
		this.jsonObject = JsonUtils.fromString(json);
		LOGGER.info("...response: " + this.jsonObject.toString() + "\n");
		assertEquals(Status.OK.getStatusCode(), statusCode);
		 
	}






}
