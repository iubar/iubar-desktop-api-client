package it.iubar.desktop.api;

 
import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class MasterClientTest extends MasterClientAbstract {


	protected static final String ECHO_API_2 = "http://www.iubar.it/extranet/api/echo2";
	
	
    private static final String user = "daniele.montesi@iubar.it";
    private static final String apiKey = "1234567890";
    
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
        	String url1 = ECHO_API_2;
        	masterClient.setAuth(true);
        	masterClient.setUser(user);			
        	masterClient.setApiKey(apiKey);        	
        	JSONObject jsonObj = masterClient.send(url1, MasterClientAbstract.client);  
        	assertNotNull(jsonObj);
        } catch (Exception e) {
        	e.printStackTrace();
        	fail();
        }
    }
    
}