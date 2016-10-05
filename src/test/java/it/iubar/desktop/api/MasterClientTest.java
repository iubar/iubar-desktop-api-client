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

    private static final String MAC = "0000099999";

  
    private static ClientModel client;
    private static ModelsList<DatoreModel> datori;
    private static ModelsList<TitolareModel> titolari;
    private static ModelsList<CcnlModel> contratti;
    private static ModelsList<DocModel> documenti;

    @BeforeClass
    public static void genFiles() throws IOException {

//        testIni = File.createTempFile("testIni", ".ini");
//        BufferedWriter bwTest = new BufferedWriter(new FileWriter(testIni));
//        bwTest.write("host = httpbin.org\n" + "path = /post\n" + "is_auth = false\n");
//        bwTest.close();

        client = ClientModel.fatory();
        
        datori = new ModelsList<DatoreModel>(MAC, "datori");
        DatoreModel datore = DatoreModelTest.factory();
        datori.add(datore);
        
        titolari= new ModelsList<TitolareModel>(MAC, "titolari");
        TitolareModel titolare = TitolareModel.factory();
        titolari.add(titolare);
        
        contratti = new ModelsList<CcnlModel>(MAC, "contratti");
        CcnlModel ccnl = CcnlModelTest.factory();
        contratti.add(ccnl);
        
        documenti = new ModelsList<DocModel>(MAC, "doc");        
        DocModel doc = DocModelTest.factory();
        documenti.add(doc);
    }

//    @Test (expected = RuntimeException.class)
//    public void masterClientFail() throws Exception {
//        new MasterClient(brokenIni.getAbsolutePath());
//    }

//    @Test
//    public void testNormalizeHost() throws Exception{
//        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
//        Method method = MasterClient.class.getDeclaredMethod("normalizeHost", String.class);
//        method.setAccessible(true);
//        assertEquals("localhost", method.invoke(masterClient, "http://localhost:80"));
//        assertEquals("localhost", method.invoke(masterClient, "https://localhost"));
//    }

//    @Test
//    public void testNormalizePort() throws Exception {
//        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
//        Method method = MasterClient.class.getDeclaredMethod("normalizePort", String.class);
//        method.setAccessible(true);
//        assertEquals("8080", method.invoke(masterClient, ":8080"));
//    }

//    @Test
//    public void testNormalizePath() throws Exception {
//        MasterClient masterClient = new MasterClient(testIni.getAbsolutePath());
//        Method method = MasterClient.class.getDeclaredMethod("normalizePath", String.class);
//        method.setAccessible(true);
//        assertEquals("/yo", method.invoke(masterClient, "yo"));
//    }
//

    @Test
    public void sendTestOnEchoServer() throws Exception {
        MasterClient masterClient = MasterClientTest.factory();
        try {
       	
        	// Test on Echo server
        
        	String url1 = masterClient.getBaseUrl() + "pagheopen" + it.iubar.desktop.api.MasterClient.INSERT_CLIENT;

            masterClient.send(url1, MasterClientTest.client);
 
//            masterClient.send(url2, MasterClientTest.titolari);
 
            //masterClient.send(url3, MasterClientTest.datori);
            //masterClient.send(url4, MasterClientTest.contratti);
            //masterClient.send(url5, MasterClientTest.documenti);

        } catch (Exception e) {
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
			// masterClient.setBaseUrl("http://104.155.64.146:81/crm/api/public/crm/v1");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return masterClient;
	}

	@Test
    public void sendTestOnEchoServer2() throws Exception {
        MasterClient masterClient = MasterClientTest.factory();
        try {
       	
        	// Test on Echo server
        
        	String url1 = masterClient.getBaseUrl() + "pagheopen" + it.iubar.desktop.api.MasterClient.INSERT_CLIENT;
			String user = "";
			String apiKey = "";
        	masterClient.setAuth(true);
        	masterClient.setUser(user);			
        	masterClient.setApiKey(apiKey);        	
            masterClient.send(url1, MasterClientTest.client);  

        } catch (Exception e) {
            fail();
        }
    }
 
	
	
    @Ignore("Ignoring test until the service is online")
    public void sendTest() throws Exception {
    	MasterClient masterClient = MasterClientTest.factory();
        try {
        	
        	// Test on Production 
        	
            masterClient.send(MasterClientTest.client);
            masterClient.send(MasterClientTest.titolari);
            masterClient.send(MasterClientTest.datori);
            masterClient.send(MasterClientTest.contratti);
            masterClient.send(MasterClientTest.documenti);
                        
        } catch (ClientException e) {
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

//    @Ignore("Ignoring test until the service is online")
//    @Test (expected = RuntimeException.class)
//    public void checkWrongMac() throws Exception {
//        MasterClient masterClient = new MasterClient("src/main/resources/config.ini");
//        ListMac listMac = masterClient.checkMac("123456789");
//    }

 
    @AfterClass
    public static void delFiles(){
   
    }
}