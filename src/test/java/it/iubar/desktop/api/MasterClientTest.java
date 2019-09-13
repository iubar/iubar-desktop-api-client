package it.iubar.desktop.api;

 
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MasterClientTest extends MasterClientAbstract {


    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    	MasterClientAbstract.loadConfig();
    }
    
	@Override
	protected IHttpClient clientFactory() {
		HmacClient masterClient = null;
    	 
			masterClient = new HmacClient();
			// masterClient.loadConfig();
			// oppure
			// masterClient.setBaseUrl("http://httpbin.org/post");
			// oppure
			masterClient.setBaseUrl(RestApiConsts.CRM_BASE_ROUTE);
 
    	return masterClient;
	}
 
    @Test
    public void sendTestOnEchoServer2(){
    	HmacClient masterClient = (HmacClient) clientFactory();
        try {       	
        	// Test on Echo server        
        	String url1 = HttpClientTest.ECHO_API_2;
        	masterClient.setAuth(true);
        	// Non deve soprendere, ma utilizzo le stesse credenziali 
        	// sia per autenticazione HMAC che per JWT
        	masterClient.setUser(MasterClientAbstract.user);			
        	masterClient.setApiKey(MasterClientAbstract.apiKey);        	
        	JSONObject jsonObj = masterClient.send(url1, MasterClientAbstract.client);  
        	assertNotNull(jsonObj);
        } catch (Exception e) {
        	e.printStackTrace();
        	fail();
        }
    }
    
}