package it.iubar.desktop.api;

 
import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

public class MasterClientTest extends MasterClientAbstract {


	protected static final String ECHO_API_2 = "http://www.iubar.it/extranet/api/echo2";

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
        	String url1 = ECHO_API_2;
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
    
}