package it.iubar.desktop.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.logging.Logger;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
 
import org.junit.jupiter.api.BeforeAll;

import it.iubar.desktop.api.json.JsonUtils;
import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;
import jakarta.json.JsonObject;

public class HttpMethods {
	
	private static JsonObject jsonObject = null;

	private static final Logger LOGGER = Logger.getLogger(HttpMethods.class.getName());
	

		
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		MasterClientAbstract.loadConfig();
	}

	protected static IHttpClient clientFactory() {
		HmacClient masterClient = null;
		masterClient = new HmacClient();
		// masterClient.loadConfig();
		masterClient.setBaseUrl(RestApiConsts.CRM_BASE_ROUTE);
		return masterClient;
	}

	public static void modelSend(String url, IJsonModel model) {
		HmacClient masterClient = (HmacClient) clientFactory();
		masterClient.setAuth(false);
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
		boolean data = false;
		assertNotNull(jsonObjModel);		
		if(jsonObjModel!=null) {
			data = jsonObjModel.getBoolean("data");
		}
		assertTrue(data);

	}

	public static void modlesSend(ModelsList models) {
		HmacClient masterClient = (HmacClient) clientFactory();
		masterClient.setAuth(false);
		JsonObject jsonObjModel = null;
		try {
			jsonObjModel = masterClient.send(models);
			boolean data = jsonObjModel.getBoolean("data");
			assertNotNull(jsonObjModel);
			assertTrue(data);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.getMessage());
		}
	}

	public static void send(String input, String path, boolean request) {
		Client client = HttpClient.newClient();

		URI baseUri = UriBuilder.fromUri(RestApiConsts.CRM_BASE_ROUTE).build();
		WebTarget target = client.target(baseUri);

		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		Response response = target.path(path).request().accept(MediaType.APPLICATION_JSON).post(Entity.json(input));
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		HttpMethods.jsonObject = JsonUtils.fromString(json);
		LOGGER.info("...request: " + input + " | response: " + HttpMethods.jsonObject.toString() + "\n");

		String message = HttpMethods.jsonObject.getString("response");
		boolean data;

		if (request) {
			data = HttpMethods.jsonObject.getBoolean("data");
			isOk(message, statusCode, data);
		} else {
			isBadRequest(message, statusCode);
		}
	}

	public static void isBadRequest(String message, int statusCode) {
		assertNotNull(message);
		LOGGER.info("Message: " + message);
		String txt = "Status: " + statusCode + "\n2 :" + HttpMethods.jsonObject.getInt("code");
		LOGGER.info(txt);
		assertEquals(Status.BAD_REQUEST.getStatusCode(), statusCode);
		assertEquals(Status.BAD_REQUEST.getStatusCode(), HttpMethods.jsonObject.getInt("code"));
	}

	public static void isOk(String message, int statusCode, boolean data) {
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertTrue(data);
		assertEquals(Status.OK.getStatusCode(), HttpMethods.jsonObject.getInt("code"));
	}

	public static void isDataFalse() {

		boolean data = HttpMethods.jsonObject.getBoolean("data");
		assertFalse(data);
	}

	public static void receive(String path) {
		Client client = HttpClient.newClient();

		URI baseUri = UriBuilder.fromUri(RestApiConsts.CRM_BASE_ROUTE).build();
		WebTarget target = client.target(baseUri);

		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");

		Response response = target.path(path).request().accept(MediaType.APPLICATION_JSON).get(Response.class);

		int statusCode = response.getStatus();

		String json = response.readEntity(String.class);
		HttpMethods.jsonObject = JsonUtils.fromString(json);

		LOGGER.info("...response: " + HttpMethods.jsonObject.toString() + "\n");

		String message = HttpMethods.jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), HttpMethods.jsonObject.getInt("code"));
	}
}
