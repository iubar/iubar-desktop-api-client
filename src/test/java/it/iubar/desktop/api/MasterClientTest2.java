package it.iubar.desktop.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import it.iubar.desktop.api.models.CcnlModel;
import it.iubar.desktop.api.models.CcnlModelTest;
import it.iubar.desktop.api.models.ClientModel;
import it.iubar.desktop.api.models.ClientModelTest;
import it.iubar.desktop.api.models.DatoreModel;
import it.iubar.desktop.api.models.DatoreModelTest;
import it.iubar.desktop.api.models.DocModel;
import it.iubar.desktop.api.models.DocModelTest;
import it.iubar.desktop.api.models.ModelsList;
import it.iubar.desktop.api.models.TitolareModel;
import it.iubar.desktop.api.models.TitolareModelTest;

public class MasterClientTest2 {

	private static final Logger LOGGER = Logger.getLogger(MasterClientTest2.class.getName());
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MasterClientAbstract.loadConfig();
	}

	protected IHttpClient clientFactory() {
		HmacClient masterClient = null;
		masterClient = new HmacClient();
		// masterClient.loadConfig();
		masterClient.setBaseUrl(RestApiConsts.CRM_BASE_ROUTE);
		return masterClient;
	}
	
	

	@Test
	public void sendClient() {
		HmacClient masterClient = (HmacClient) clientFactory();
		try {
			String url1 = "http://iubar.it/crm/api/crm/v1/client";
			masterClient.setAuth(false);
			ClientModel client = ClientModelTest.factory();
			JSONObject jsonObjClient = masterClient.send(url1, client);
			boolean data = jsonObjClient.getBoolean("data");
			
			assertNotNull(jsonObjClient);
			assertTrue(data);			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void sendContatto() {
		HmacClient masterClient = (HmacClient) clientFactory();
		try {
			masterClient.setAuth(false);
			CcnlModel contratto = CcnlModelTest.factory();
			ModelsList<CcnlModel> contratti = new ModelsList<CcnlModel>(ClientModelTest.MAC,
					MasterClientAbstract.ID_APP_PAGHEOPEN, "contratti");
			contratti.add(contratto);
			JSONObject jsonObjContratti = masterClient.send(contratti);
			boolean data = jsonObjContratti.getBoolean("data");
			
			assertNotNull(jsonObjContratti);
			assertTrue(data);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	@Test
	public void sendDatore() {
		HmacClient masterClient = (HmacClient) clientFactory();
		try {
			masterClient.setAuth(false);
			DatoreModel datore = DatoreModelTest.factory();
			ModelsList<DatoreModel> datori = new ModelsList<DatoreModel>(ClientModelTest.MAC,
					MasterClientAbstract.ID_APP_PAGHEOPEN, "datori");
			datori.add(datore);
			JSONObject jsonObjdatori = masterClient.send(datori);
			boolean data = jsonObjdatori.getBoolean("data");
			
			assertNotNull(jsonObjdatori);
			assertTrue(data);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void sendDoc() {
		HmacClient masterClient = (HmacClient) clientFactory();
		try {
			masterClient.setAuth(false);
			DocModel doc = DocModelTest.factory();

			JSONObject jsonObjDocs = masterClient.send(doc);
			boolean data = jsonObjDocs.getBoolean("data");
			assertNotNull(jsonObjDocs);
			assertTrue(data);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void receiveIncrementDocumento()
	{
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String input = "{" + "\"iddoctype\": \"1\", " + "\"qnt\": \"0\""+ "}";
		String path = "increment-documento";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(input));				
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);		
		LOGGER.info("...request: " + input + " | response: " + jsonObject.toString()+"\n");
				
		String message = jsonObject.getString("response");
		boolean data = jsonObject.getBoolean("data");				
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertTrue(data);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
		
	}

	@Test
	public void receiveRegisterClientFail() {
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String input = "{" + "\"mac\": \"B8-CA-3A-96-BD-03\", " + "\"cf\": \"DMNLSN95P14D969J\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(input));
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...request: " + input + " | response: " + jsonObject.toString()+"\n");
		
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(statusCode, Status.BAD_REQUEST.getStatusCode());
		assertEquals(Status.BAD_REQUEST.getStatusCode(), jsonObject.getInt("code"));
	}

	@Test
	public void sendRegisterClient() {
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String input = "{" + "\"mac\": \"" + ClientModelTest.MAC + "\", " + "\"cf\": \"" + ClientModelTest.CF + "\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "register-client/"+Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.json(input));
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...request: " + input + " | response: " + jsonObject.toString() +"\n");
		
		String message = jsonObject.getString("response");
		boolean data = jsonObject.getBoolean("data");
		
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertTrue(data);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}
	@Test
	public void sendTitolare() {
		HmacClient masterClient = (HmacClient) clientFactory();
		try {
			masterClient.setAuth(false);
			TitolareModel titolare = TitolareModelTest.factory();
			
			ModelsList<TitolareModel> titolari = new ModelsList<TitolareModel>(ClientModelTest.MAC,
					MasterClientAbstract.ID_APP_PAGHEOPEN, "titolari");
			titolari.add(titolare);
			
			JSONObject jsonObjTitolari = masterClient.send(titolari);
			boolean data = jsonObjTitolari.getBoolean("data");
			
			assertNotNull(jsonObjTitolari);
			assertTrue(data);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	

	@Test
	public void receiveAggiornamentiMese()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path =Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE)+ "/stats/updated/"+"2017-01-01/"+"2017-05-26";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}
	
	@Test
	public void receiveAnalisi()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path =Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE)+ "/analytics";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");
		
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));		
	}

	@Test
	public void receiveBlacklist()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path =Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE)+ "/blacklist/" + "mac/" + ClientModelTest.MAC;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");
		
		boolean data = jsonObject.getBoolean("data");
		
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertFalse(data);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));		
	}

	@Test
	public void receiveCedolino()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path ="stats/cedolini/" + "2016-05-06/"+"2017-05-06" ;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");
		
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));	
	}
		
	@Test 
	public void receiveCountDocumentiMese()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path ="count-documenti/" +"1/"+ "2017-08-19/"+"2017-09-19" ;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");
		
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));	
	}
	
	@Test 
	public void receiveDocumenti()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "stats/doc/" + "2016-05-06/"+"2017-05-06" ;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");
		
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));	
	}

	@Test
	public void receiveElencoProvincie()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "provincia/all" ;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");
		
		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));	
	}

	@Test
	public void receiveGreylist()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/greylist/" + "mac/" + ClientModelTest.MAC;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));			
	}
	

	@Test
	public void receiveInfoMac()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "info/" + ClientModelTest.MAC;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");

		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));	
	}

	@Test
	public void receiveInfoTitolare()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "info/" + "cognome/" + "prova";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.header("PRIVATE-TOKEN", null).get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));	
	}
	
	@Test
	public void receiveInstallazioniMese()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed/" + "2017-01-01/" + "2017-05-26";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));	
	}

	@Test
	public void receiveInstallazioni()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}
	
	@Test
	public void receiveNow()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "now/" + "europe/" + "rome" ;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}
	
	
	@Test
	public void receivePreventivi()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "stats/preventivi/" + "2016-05-06/" + "2017-05-06" ;
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}
	
	@Test
	public void receiveStatisticheGenerali()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) +"/stats/" + "2016-05-06/" + "2017-05-06";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}
	
	@Test
	public void receiveTopUsers()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "top-users/limit/" + "10";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}
	@Test
	public void receiveUltimiUtenti()
	{
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		URI baseUri = UriBuilder.fromUri("http://iubar.it/crm/api/crm/v1").build();
		WebTarget target = client.target(baseUri);
		String path = "last-users/limit/" + "10";
		LOGGER.info("Testing path \"" + baseUri.toString() + "/" + path + "\" ...");
		
		Response response = target.path(path)
				.request().accept(MediaType.APPLICATION_JSON)
				.get(Response.class);
		
		int statusCode = response.getStatus();
		String json = response.readEntity(String.class);
		JSONObject jsonObject = new JSONObject(json);
		LOGGER.info("...response: " + jsonObject.toString()+"\n");
		String message = jsonObject.getString("response");

		assertNotNull(message);
		assertEquals(Status.OK.getStatusCode(), statusCode);
		assertEquals(Status.OK.getStatusCode(), jsonObject.getInt("code"));
	}

	
}
