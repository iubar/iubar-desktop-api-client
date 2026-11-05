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
		 masterClient.setBaseUrl(RestApiConsts.API_BASE_ROUTE); 
		return masterClient;
	}

 
	
	@Test
	public void receiveNow() {
		String path = "/now/europe/rome";
		try {
			HttpMethods obj = new HttpMethods(RestApiConsts.API_BASE_ROUTE);
			obj.receive(path);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}
	}
	
	@Test
	public void sendTestOnEchoServer() throws Exception{
		// Test on Echo server        
 
		String  url1 = "/echo";
		HttpMethods  obj = new HttpMethods (RestApiConsts.API_BASE_ROUTE);
		JsonObject jsonObj = obj.masterClient.send(url1, MasterClientAbstract.client);
		assertNotNull(jsonObj);
	}
	
}
