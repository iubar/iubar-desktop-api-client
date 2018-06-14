package it.iubar.desktop.api;

 
import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class MasterJwtClientTest extends MasterClientAbstract {
    
	protected static final String ROUTE_1 = "http://www.iubar.it/extranet/api/jwt/token"; // POST
	protected static final String ECHO_API_3 = "http://www.iubar.it/extranet/api/jwt/data"; // ANY
 
    
    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    	MasterClientAbstract.loadConfig();
    }
    
    @Test
    public void sendTestOnEchoJwtServer(){
    	JwtClient masterClient = (JwtClient) clientFactory();
        try {       	    
        	// Test on Echo server        
        	String url1 = ECHO_API_3;
        	masterClient.setAuth(true);
        	masterClient.setUser(MasterClientAbstract.user);			
        	masterClient.setApiKey(MasterClientAbstract.apiKey);         	
        	JSONObject jsonObj = masterClient.send(url1, MasterClientAbstract.client);

        	assertNotNull(jsonObj);
        } catch (Exception e) {
        	e.printStackTrace();
        	fail();
        }
    }
    
    @Test
    public void sendTestOnEchoJwtServer2(){
    	String url1 = ECHO_API_3 + "?msg=HelloWorld";  	
    	JwtClient masterClient = (JwtClient) clientFactory();
    	masterClient.setAuth(true);
    	masterClient.setUser(MasterClientAbstract.user);			
    	masterClient.setApiKey(MasterClientAbstract.apiKey);
   	    
    	// Test on Echo server        
    	masterClient.setAuth(true);
    	Response response = masterClient.get(url1);
    	        	
		int status = response.getStatus();
		String output = response.readEntity(String.class);
		JSONObject answer = new JSONObject(output);
		System.out.println("Response status: " + status);
		System.out.println("Response data: " + output);
		System.out.println("Answer: " + answer);
		
    	assertEquals(200, response.getStatus());
    }
 
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