package it.iubar.desktop.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

public class MasterClientTest2 extends MasterClientAbstract {
	
	
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		MasterClientAbstract.loadConfig();
	}
	
	
	public MasterClientTest2() {
		super();
		}
	
	
	@Override
	protected IHttpClient clientFactory() {
		HttpClient2025 masterClient = new HttpClient2025();
		masterClient.setBaseUrl(RestApiConsts.CRM_BASE_ROUTE);
		return masterClient;
	}



	@Test
	public void sendClient() {
		ClientModel client = ClientModelTest.factory();
		String url = AuthHttpClient.INSERT_CLIENT; // questo è solo un test, potrei tranquillamente invocare  HttpMethods.modelSend(null, client); senza specificare la rotta
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.modelSend(url, client);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

 

	@Test
	public void sendRegisterClientFail() {
		String input = "{" + "\"mac\": \"B8-CA-3A-96-BD-03\", " + "\"cf\": \"DMNLSN95P14D969J\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "/public/register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.send(input, path, false);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void sendRegisterClient() {
		String input = "{" + "\"mac\": \"" + ClientModelTest.MAC + "\", " + "\"cf\": \"" + ClientModelTest.CF + "\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "/public/register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
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
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.modlesSend(titolari);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

 

	@Test
	public void receiveInfoMac() {
		String path = "/public/paghe/info/mac/" + ClientModelTest.MAC;
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receive(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	

	@Test
	public void receiveNow() {
		String path = "/public/now/europe/rome";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receive(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void receiveAggiornamentiMese() {
		String path = MasterClientAbstract.APP_FAMILY_PAGHE  + "/stats/updated/" + "2017-01-01/" + "2017-05-26";
		try {		
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveAnalisi() {
		String path = MasterClientAbstract.APP_FAMILY_PAGHE + "/analytics";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveCedolino() {
		String path = "stats/cedolini/" + "2016-05-06/" + "2017-05-06";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveCountDocumentiMese() {
		String path = "count-documenti/" + "1/" + "2017-08-19/" + "2017-09-19";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInfoTitolare() {
		String path = "info/cognome/" + "Corsini";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInstallazioniMese() {
		String path = MasterClientAbstract.APP_FAMILY_PAGHE  + "/stats/installed/" + "2017-01-01/" + "2017-05-26";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInstallazioni() {
		String path = MasterClientAbstract.APP_FAMILY_PAGHE  + "/stats/installed";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveStatisticheGenerali() {
		String path =  MasterClientAbstract.APP_FAMILY_PAGHE + "/stats/" + "2016-05-06/" + "2017-05-06";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
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
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
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
			HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);
			obj.receiveProtected(path, queryParams );
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void sendTestOnEchoServer() throws Exception{
		// Test on Echo server        
		String  SANDBOX_BASE_API_URL = RestApiConsts.CRM_BASE_ROUTE_ROOT + "/sandbox";
		String  url1 = SANDBOX_BASE_API_URL + "/echo";
		HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE); 
		JsonObject jsonObj = obj.masterClient.send(url1, MasterClientAbstract.client);
		assertNotNull(jsonObj);
	}
	
 
	
	@Test
	public void sendTest( ) throws Exception {
	       initData();	
		// Test on Production 
		HttpMethods obj = new HttpMethods(RestApiConsts.CRM_BASE_ROUTE);		
		JsonObject jsonObj1 = obj.masterClient.send(MasterClientAbstract.client);
		assertNotNull(jsonObj1);
		assertTrue(jsonObj1.getInt("data")>=0);
		JsonObject jsonObj2 = obj.masterClient.send(MasterClientAbstract.titolari);
		assertNotNull(jsonObj2);
		assertTrue(jsonObj2.getInt("data")>=0);
	}
	
}
