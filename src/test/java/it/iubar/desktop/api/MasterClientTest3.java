package it.iubar.desktop.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import it.iubar.desktop.api.models.ClientModel;
import it.iubar.desktop.api.models.ClientModelTest;
import it.iubar.desktop.api.models.ModelsList;
import it.iubar.desktop.api.models.TitolareModel;
import it.iubar.desktop.api.models.TitolareModelTest;
import jakarta.json.JsonObject;

public class MasterClientTest3  extends MasterClientAbstract {
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		MasterClientAbstract.loadConfig();
	}
	
	@Override
	protected IHttpClient clientFactory() {
		HttpClient2025 masterClient = new HttpClient2025();
		 masterClient.setBaseUrl(RestApiConsts.CRM_BASE_ROUTE2); 
		return masterClient;
	}

	@Test
	public void sendClient() {
		ClientModel client = ClientModelTest.factory();
		
		String url = AuthHttpClient.INSERT_CLIENT; // questo è solo un test, potrei tranquillamente invocare  HttpMethods.modelSend(null, client); senza specificare la rotta
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.modelSend(url, client);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void sendIncrementDocumento() {
		String input = "{" + "\"iddoctype\": \"1\", " + "\"qnt\": \"0\"" + "}";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.send(input, AuthHttpClient.INCREMENT_DOC, true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void sendRegisterClientFail() {
		String input = "{" + "\"mac\": \"B8-CA-3A-96-BD-03\", " + "\"cf\": \"DMNLSN95P14D969J\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "/public/register-client";

		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.send(input, path, false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		//	fail(e.getMessage());
		}
	}

	@Test
	public void sendRegisterClient() {
		String input = "{" + "\"mac\": \"" + ClientModelTest.MAC + "\", " + "\"cf\": \"" + ClientModelTest.CF + "\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "/public/register-client"  ;

		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.send(input, path, true);
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void sendTitolare() {
		TitolareModel titolare = TitolareModelTest.factory();
		ModelsList<TitolareModel> titolari = new ModelsList<TitolareModel>(ClientModelTest.MAC, MasterClientAbstract.ID_APP_PAGHEOPEN, "titolari");
		titolari.add(titolare);		
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2 );
					obj.modlesSend(titolari);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveBlacklist() {
		String path = "/public/blacklist/" + "mac/"
				+ ClientModelTest.MAC;
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receive(path);
			obj.isDataFalse();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	

	@Test
	public void receiveInfoMac() {
		String path = "public/info/mac/" + ClientModelTest.MAC;
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receive(path);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}
	

	@Test
	public void receiveNow() {
		String path = "/public/now/europe/rome";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receive(path);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}
	
	@Test
	public void receiveAggiornamentiMese() {
		String path = "/stats/updated/" + "2017-01-01/" + "2017-05-26";
		try {		
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveAnalisi() {
		String path =  "/analytics";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveCedolino() {
		String path = "/stats/cedolini/" + "2016-05-06/" + "2017-05-06";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveCountDocumentiMese() {
		String path = "/count-documenti/" + "1/" + "2017-08-19/" + "2017-09-19";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInfoTitolare() {
		String path = "info/cognome/" + "Corsini";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInstallazioniMese() {
		String path =  "/stats/installed/" + "2017-01-01/" + "2017-05-26";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInstallazioni() {
		String path =   "/stats/installed";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveStatisticheGenerali() {
		String path =    "/stats/" + "2016-05-06/" + "2017-05-06";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	

	@Test
	public void receiveTopUsers() {
		// String path = "top-users?limit=10"; // se volessi usare questa stringa dovrei invocare HttpMethods.dummyTooComplicated()
		String path = "top-users";
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("limit", "10");
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path, queryParams);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveUltimiUtenti() {
		// String path = "last-users?limit=10"; // se volessi usare questa stringa dovrei invocare HttpMethods.dummyTooComplicated()
		String path = "last-users";
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("limit", "10");
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
			obj.receiveProtected(path, queryParams );
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void sendTestOnEchoServer() throws Exception{
		// Test on Echo server        
		String  SANDBOX_BASE_API_URL = "https://api.iubar.it";
		String  url1 = SANDBOX_BASE_API_URL + "/api/echo";

		HttpMethods  obj = new HttpMethods (RestApiConsts.CRM_BASE_ROUTE2);
		JsonObject jsonObj = obj.masterClient.send(url1, MasterClientAbstract.client);
		assertNotNull(jsonObj);
	}
	
	@Test
	public void testMacBlacklist_1() throws Exception{ 	
		HttpMethods  obj = new HttpMethods (RestApiConsts.CRM_BASE_ROUTE2);
		String url = "/public/blacklist/mac/";
		boolean b = obj.masterClient.checkMacBlacklist(url + MAC_1);
		assertFalse(b);
	}

	@Test
	public void testMacBlacklist_2() throws Exception{ 	
		HttpMethods  obj = new HttpMethods (RestApiConsts.CRM_BASE_ROUTE2);
		String url = "/public/blacklist/mac/";
		boolean b = obj.masterClient.checkMacBlacklist(url + MAC_2);
		assertFalse(b);
	}  
	
	@Test
	public void sendTest( ) throws Exception {
		 initData();	
		// Test on Production 
		HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE2);
		JsonObject jsonObj1 = obj.masterClient.send(MasterClientAbstract.client);
		assertNotNull(jsonObj1);
		assertNotEquals(JsonObject.EMPTY_JSON_OBJECT, jsonObj1); 
		assertTrue(jsonObj1.getInt("data")>=0);
		JsonObject jsonObj2 = obj.masterClient.send(MasterClientAbstract.titolari);
		assertNotEquals(JsonObject.EMPTY_JSON_OBJECT, jsonObj2);
		assertNotNull(jsonObj2);
		assertTrue(jsonObj2.getInt("data")>=0);
	}	
	
}
