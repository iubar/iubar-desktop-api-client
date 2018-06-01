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
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public abstract class MasterClientAbstract {

    private static final String MAC_1 = "0000099999";
    private static final String MAC_2 = "0000099999";
    private static final String MAC_3 = "0000099999";
    
	private static final String ECHO_API = "http://www.iubar.it/extranet/api/echo";

	private static final String APP_FAMILY_PAGHE = "paghe";
	public static final int ID_APP_PAGHEOPEN = 11;
	public static final int ID_FAMILY_PAGHE = 1;
	public static String user = null;
	public static String apiKey = null;


	  
	protected static ClientModel client = null;
    private static ModelsList<DatoreModel> datori = null;
    private static ModelsList<TitolareModel> titolari = null;
    private static ModelsList<CcnlModel> contratti = null;
    private static ModelsList<DocModel> documenti = null;

    public static void loadConfig() throws Exception {
    	String jwtUser = System.getenv("JWT_USER");
    	String jwtApiKey = System.getenv("JWT_APIKEY");
    	if(jwtUser!=null && jwtUser.length()>0) {
    		MasterClientAbstract.user = jwtUser;
    		MasterClientAbstract.apiKey = jwtApiKey;
    	}else {
    		String config = "/secret.properties";    		
    		Properties prop = new Properties();
    		InputStream is = null;
    		try {
    			is = MasterJwtClientTest.class.getResourceAsStream(config);
    			prop.load(is);
    			MasterClientAbstract.user = prop.getProperty("JWT_USER");
    			MasterClientAbstract.apiKey = prop.getProperty("JWT_APIKEY");

    		} catch (IOException ex) {
    			ex.printStackTrace();
    		} finally {
    			if (is != null) {
    				try {
    					is.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    	}
    }
    
    @BeforeClass
    public static void initData() throws IOException {
    	int idApp = MasterClientAbstract.ID_APP_PAGHEOPEN;
    	MasterClientAbstract.client = ClientModelTest.factory();
        
    	MasterClientAbstract.datori = new ModelsList<DatoreModel>(MAC_1, idApp, "datori");
        DatoreModel datore = DatoreModelTest.factory();
        MasterClientAbstract.datori.add(datore);
        
        MasterClientAbstract.titolari= new ModelsList<TitolareModel>(MAC_1, idApp, "titolari");
        TitolareModel titolare = TitolareModelTest.factory();
        MasterClientAbstract.titolari.add(titolare);
        
        MasterClientAbstract.contratti = new ModelsList<CcnlModel>(MAC_1, idApp, "contratti");
        CcnlModel ccnl = CcnlModelTest.factory();
        MasterClientAbstract.contratti.add(ccnl);
        
        MasterClientAbstract.documenti = new ModelsList<DocModel>(MAC_1, 0, "doc");        
        DocModel doc = DocModelTest.factory();
        MasterClientAbstract.documenti.add(doc);
    }


    abstract protected IHttpClient clientFactory();
    

    
	private boolean checkMacBlacklist(String mac){
		boolean b = false;
    	IHttpClient masterClient = clientFactory();
		try {
			JSONObject jsonObject = masterClient.responseManager(masterClient.get(APP_FAMILY_PAGHE + "/blacklist/mac/" + mac));
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
    
   
    private int checkMacGreylist(String mac){ 	
    	int idreason = 0; // false
    	IHttpClient masterClient = clientFactory();
		try{		
	    	JSONObject jsonObject = masterClient.responseManager(masterClient.get(APP_FAMILY_PAGHE + "/greylist/mac/" + mac));
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
	


    @Ignore("Ignoring test until the service is online")
    public void sendTest() {
    	IHttpClient masterClient = clientFactory();
        try {
        	
        	// Test on Production 
        	
            JSONObject jsonObj1 = masterClient.send(MasterClientAbstract.client);
            assertNotNull(jsonObj1);
            assertTrue(jsonObj1.has("response"));
            JSONObject jsonObj2 = masterClient.send(MasterClientAbstract.titolari);
            assertNotNull(jsonObj2);
            assertTrue(jsonObj2.has("response"));
            JSONObject jsonObj3 = masterClient.send(MasterClientAbstract.datori);
            assertNotNull(jsonObj3);            
            assertTrue(jsonObj3.has("response"));
            JSONObject jsonObj4 = masterClient.send(MasterClientAbstract.contratti);
            assertNotNull(jsonObj4);
            assertTrue(jsonObj4.has("response"));
            JSONObject jsonObj5 = masterClient.send(MasterClientAbstract.documenti);
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
//        IMasterClient masterClient = new MasterClient("src/main/resources/config.ini");
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
       

       @Test
       public void sendTestOnEchoServer(){
           IHttpClient masterClient = clientFactory();
           try {
           	// Test on Echo server        
           	String url1 = ECHO_API;
           	JSONObject jsonObj = masterClient.send(url1, MasterClientAbstract.client);
           	assertNotNull(jsonObj);
           } catch (Exception e) {
           	e.printStackTrace();
           	fail();
           }
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
       
       
 
    @AfterClass
    public static void delFiles(){
   
    }
}