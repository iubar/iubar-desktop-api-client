package it.iubar.desktop.api;

 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;

import it.iubar.desktop.api.json.JsonUtils;
import jakarta.json.JsonObject;

public class MasterJwtClientTest extends MasterClientAbstract {
        
    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    	MasterClientAbstract.loadConfig();
    }
    
    @Test
    public void sendTestOnEchoJwtServer(){
    	JwtClient masterClient = (JwtClient) clientFactory();
        try {       	    
        	// Test on Echo server        
        	String url1 = HttpClientTest.ECHO_API_3;
        	masterClient.setAuth(true);
        	masterClient.setUser(MasterClientAbstract.user);			
        	masterClient.setApiKey(MasterClientAbstract.apiKey);         	
        	JsonObject jsonObj = masterClient.send(url1, MasterClientAbstract.client);
        	assertNotNull(jsonObj);
        } catch (Exception e) {
        	e.printStackTrace();
        	fail();
        }
    }
    
    @Test
    public void sendTestOnEchoJwtServer2(){
    	String url1 = HttpClientTest.ECHO_API_3 + "?msg=HelloWorld";  	
    	JwtClient masterClient = (JwtClient) clientFactory();
    	masterClient.setAuth(true);
    	masterClient.setUser(MasterClientAbstract.user);			
    	masterClient.setApiKey(MasterClientAbstract.apiKey);
   	    
    	// Test on Echo server        
    	masterClient.setAuth(true);
    	Response response = masterClient.get(url1);
    	        	
		int status = response.getStatus();
		String output = response.readEntity(String.class);
		
		JsonObject answer = JsonUtils.parseJsonString(output);
 
		System.out.println("Response status: " + status);
		System.out.println("Response data: " + output);
		System.out.println("Answer: " + answer);
		
    	assertEquals(200, response.getStatus());
    }
 
	@Override
	protected IHttpClient clientFactory() {
    	JwtClient masterClient = null;
    	 
			masterClient = new JwtClient();
			// masterClient.loadConfig();
			// oppure
			// masterClient.setBaseUrl("http://httpbin.org/post");
			// oppure
			masterClient.setBaseUrl(RestApiConsts.CRM_BASE_ROUTE);
 
    	return masterClient;
	}
 
    @AfterAll
    public static void delFiles(){
   
    }
}