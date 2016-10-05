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

public class MasterClientTest {

    private static final String MAC_1 = "0000099999";
    private static final String MAC_2 = "0000099999";
    private static final String MAC_3 = "0000099999";
    
    private static final String user = "daniele.montesi@iubar.it";
    private static final String apiKey = "1234567890";

	private static final String APP_FAMILY_PAGHE = "paghe";
	private static final String ECHO_API = "http://www.iubar.it/extranet/api/echo";
	private static final String ECHO_API_2 = "http://www.iubar.it/extranet/api/echo2";
  
    private static ClientModel client;
    private static ModelsList<DatoreModel> datori;
    private static ModelsList<TitolareModel> titolari;
    private static ModelsList<CcnlModel> contratti;
    private static ModelsList<DocModel> documenti;

    @BeforeClass
    public static void genFiles() throws IOException {

        client = ClientModel.fatory();
        
        datori = new ModelsList<DatoreModel>(MAC_1, "datori");
        DatoreModel datore = DatoreModelTest.factory();
        datori.add(datore);
        
        titolari= new ModelsList<TitolareModel>(MAC_1, "titolari");
        TitolareModel titolare = TitolareModel.factory();
        titolari.add(titolare);
        
        contratti = new ModelsList<CcnlModel>(MAC_1, "contratti");
        CcnlModel ccnl = CcnlModelTest.factory();
        contratti.add(ccnl);
        
        documenti = new ModelsList<DocModel>(MAC_1, "doc");        
        DocModel doc = DocModelTest.factory();
        documenti.add(doc);
    }

    @Test
    public void sendTestOnEchoServer(){
        MasterClient masterClient = MasterClientTest.factory();
        try {
        	// Test on Echo server        
        	String url1 = ECHO_API;
        	JSONObject jsonObj = masterClient.send(url1, MasterClientTest.client);
        	assertNotNull(jsonObj);
        } catch (Exception e) {
        	e.printStackTrace();
        	fail();
        }
    }


    private static MasterClient factory() {
    	MasterClient masterClient = null;
		try {
			masterClient = new MasterClient();
			// masterClient.loadConfig();
			// oppure
			// masterClient.setBaseUrl("http://httpbin.org/post");
			// oppure
			String route = "http://104.155.64.146:81/crm/api/public/crm/v1/" + APP_FAMILY_PAGHE + "/";
			masterClient.setBaseUrl(route);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return masterClient;
	}

    @Test
	public void testMacBlacklist_1(){ 	
    	boolean b = checkMacBlacklist(MAC_1);
    	assertFalse(b);
    }
    
    @Test
	public void testMacBlacklist_2(){ 	
    	boolean b = checkMacBlacklist(MAC_2);
    	assertFalse(b);
    }
    
	private boolean checkMacBlacklist(String mac){
		boolean b = false;
    	MasterClient masterClient = MasterClientTest.factory();
		try {
			JSONObject jsonObject = masterClient.responseManager(masterClient.get("blacklist/mac/" + mac));
			b = jsonObject.getBoolean("data");
			if (b) {
				System.out.println("The mac address is black-listed");
			} else {
				System.out.println("The mac address is NOT black-listed");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return b;
	}
    
    @Test
	public void testMacGreylist_1(){ 	
    	int idreason = checkMacGreylist(MAC_1);
    	assertEquals(0, idreason);
    }
    
    @Test
	public void testMacGreylist_2(){ 	
    	int idreason = checkMacGreylist(MAC_2);
    	assertEquals(0, idreason);
    }
    
    private int checkMacGreylist(String mac){ 	
    	int idreason = 0; // false
    	MasterClient masterClient = MasterClientTest.factory();
		try{		
	    	JSONObject jsonObject = masterClient.responseManager(masterClient.get("greylist/mac/" + mac));
	    	Object obj = jsonObject.get("data");
			if(obj instanceof JSONObject){
				JSONObject jsonObject2 = jsonObject.getJSONObject("data");
				if(jsonObject2.has("idreason")){			
					idreason = jsonObject2.getInt("idreason");
					System.out.println("The mac address is grey-listed. Idreason is " + idreason);
				}else{
					fail();
				}
			}else if(obj instanceof Boolean){
				boolean b = jsonObject.getBoolean("data");
				assertFalse(b);
				System.out.println("The mac address is NOT grey-listed");
			}else{
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return idreason;
	}    
	
    @Test
    public void sendTestOnEchoServer2(){
        MasterClient masterClient = MasterClientTest.factory();
        try {       	
        	// Test on Echo server        
        	String url1 = ECHO_API_2;
        	masterClient.setAuth(true);
        	masterClient.setUser(user);			
        	masterClient.setApiKey(apiKey);        	
        	JSONObject jsonObj = masterClient.send(url1, MasterClientTest.client);  
        	assertNotNull(jsonObj);
        } catch (Exception e) {
        	e.printStackTrace();
        	fail();
        }
    }

    @Ignore("Ignoring test until the service is online")
    public void sendTest() {
    	MasterClient masterClient = MasterClientTest.factory();
        try {
        	
        	// Test on Production 
        	
            JSONObject jsonObj1 = masterClient.send(MasterClientTest.client);
            assertNotNull(jsonObj1);
            assertTrue(jsonObj1.has("response"));
            JSONObject jsonObj2 = masterClient.send(MasterClientTest.titolari);
            assertNotNull(jsonObj2);
            assertTrue(jsonObj2.has("response"));
            JSONObject jsonObj3 = masterClient.send(MasterClientTest.datori);
            assertNotNull(jsonObj3);            
            assertTrue(jsonObj3.has("response"));
            JSONObject jsonObj4 = masterClient.send(MasterClientTest.contratti);
            assertNotNull(jsonObj4);
            assertTrue(jsonObj4.has("response"));
            JSONObject jsonObj5 = masterClient.send(MasterClientTest.documenti);
            assertNotNull(jsonObj5);
            assertTrue(jsonObj5.has("response"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
		}
    }
    
//    @Ignore("Ignoring test until the service is online")
//    @Test
//    public void checkMacTest() throws Exception {
//        MasterClient masterClient = new MasterClient("src/main/resources/config.ini");
//        ListMac listMac = masterClient.checkMac("1C-4B-D6-C8-8B-31");
//        assertEquals(true,listMac.isGreyList());
//        assertEquals(2,listMac.getCodeGreyList());
//        assertEquals("Non ha pagato", listMac.getDescGreyList());
//        assertEquals(false, listMac.isBlackList());
//        listMac = masterClient.checkMac("84-2B-2B-91-78-96");
//        assertEquals(false, listMac.isGreyList());
//        assertEquals(0, listMac.getCodeGreyList());
//        assertEquals("", listMac.getDescGreyList());
//        assertEquals(true, listMac.isBlackList());
//    }

 
    @AfterClass
    public static void delFiles(){
   
    }
}