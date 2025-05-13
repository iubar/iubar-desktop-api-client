package it.iubar.desktop.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;

import it.iubar.desktop.api.json.JsonUtils;
import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

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
		int count = 0;
		assertNotNull(jsonObjModel);		
		if(jsonObjModel!=null) {
			count = jsonObjModel.getInt("data");
		}
		assertTrue(count >= 0);

	}

	public static void modlesSend(ModelsList models) {
		HmacClient masterClient = (HmacClient) clientFactory();
		masterClient.setAuth(false);
		JsonObject jsonObjModel = null;
		try {
			jsonObjModel = masterClient.send(models);
			int count = jsonObjModel.getInt("data");
			assertNotNull(jsonObjModel);
			assertTrue(count >= 0);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.getMessage());
		}
	}

	public static void send(String input, String path, boolean request) {
		Client client = HttpClient.newClient();

		URI baseUri = UriBuilder.fromUri(RestApiConsts.CRM_BASE_ROUTE).build();
		WebTarget target = client.target(baseUri);

		LOGGER.info("Testing path \"" + baseUri.toString() + path + "\" ...");
		Response response = target.path(path).request().accept(MediaType.APPLICATION_JSON).post(Entity.json(input));
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		HttpMethods.jsonObject = JsonUtils.fromString(json);
		LOGGER.info("...request: " + input + " | response: " + HttpMethods.jsonObject.toString() + "\n");

		int count = 0;

		if (request) {
			count = HttpMethods.jsonObject.getInt("data");
			isOk(statusCode, count);
		} else {
			JsonObject error = HttpMethods.jsonObject.getJsonObject("error");
			isBadRequest(error.getString("description"), statusCode);
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

	public static void isOk(int statusCode, int count) {
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertTrue(count >= 0);
		assertEquals(Status.OK.getStatusCode(), HttpMethods.jsonObject.getInt("code"));
	}

	public static void isDataFalse() {

		boolean data = HttpMethods.jsonObject.getBoolean("data");
		assertFalse(data);
	}

	public static void receive(String path) {
		Client client = HttpClient.newClient();
		dummy(client, path);
	}
	
	public static void receiveProtected(String path) {
 		Client client = HttpClient.newClientProtected(MasterClientAbstract.CRM_HTTP_USER, MasterClientAbstract.CRM_HTTP_PASSWORD);
		dummy(client, path);
	}

	private static void dummy(Client client, String path) {
		URI baseUri = UriBuilder.fromUri(RestApiConsts.CRM_BASE_ROUTE).build();
		WebTarget target = client.target(baseUri);

		LOGGER.info("Testing path \"" + baseUri.toString() + "/" +  path + "\" ...");

		Response response = target.path(path).request().accept(MediaType.APPLICATION_JSON).get(Response.class);
		
		int statusCode = response.getStatus();

		String json = response.readEntity(String.class);
		HttpMethods.jsonObject = JsonUtils.fromString(json);

		LOGGER.info("...response: " + HttpMethods.jsonObject.toString() + "\n");
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), HttpMethods.jsonObject.getInt("code"));
	}
	
	
}
