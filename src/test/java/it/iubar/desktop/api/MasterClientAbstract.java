package it.iubar.desktop.api;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.iubar.desktop.api.models.ClientModel;
import it.iubar.desktop.api.models.ClientModelTest;
import it.iubar.desktop.api.models.ModelsList;
import it.iubar.desktop.api.models.TitolareModel;
import it.iubar.desktop.api.models.TitolareModelTest;
import jakarta.json.JsonObject;

public abstract class MasterClientAbstract {

	private static final Logger LOGGER = Logger.getLogger(MasterClientAbstract.class.getName());

	private static final String MAC_1 = "B8-CA-3A-96-BD-03";
	private static final String MAC_2 = MAC_1;


	private static final String APP_FAMILY_PAGHE = "paghe";
	public static final int ID_APP_PAGHEOPEN = 11;
	public static final int ID_FAMILY_PAGHE = 1;
	public static String user = null;
	public static String apiKey = null;

	protected static ClientModel client = null;
	private static ModelsList<TitolareModel> titolari = null;

	public static void loadConfig() throws IOException {
		String jwtUser = System.getenv("JWT_USER");
		String jwtApiKey = System.getenv("JWT_APIKEY");
		if(jwtUser!=null && jwtUser.length()>0) {
			MasterClientAbstract.user = jwtUser;
			MasterClientAbstract.apiKey = jwtApiKey;
		}else {
			String config = "/secret.properties";    		
			Properties prop = new Properties();
			InputStream is = MasterJwtClientTest.class.getResourceAsStream(config);
			prop.load(is);
			MasterClientAbstract.user = prop.getProperty("JWT_USER");
			MasterClientAbstract.apiKey = prop.getProperty("JWT_APIKEY");
			is.close();
		}

		if(MasterClientAbstract.user == null || MasterClientAbstract.user.length()==0) { 
			LOGGER.log(Level.SEVERE,"Impossibile continuare: user non specificato");
			System.exit(1);
		}else if(MasterClientAbstract.apiKey == null || MasterClientAbstract.apiKey.length()==0) {
			LOGGER.log(Level.SEVERE, "Impossibile continuare: apiKey non specificata");
			System.exit(1);
		}

	}

	@BeforeAll
	public static void initData() throws IOException {
		int idApp = MasterClientAbstract.ID_APP_PAGHEOPEN;
		MasterClientAbstract.client = ClientModelTest.factory();

		MasterClientAbstract.titolari= new ModelsList<TitolareModel>(MAC_1, idApp, "titolari");
		TitolareModel titolare = TitolareModelTest.factory();
		MasterClientAbstract.titolari.add(titolare);
	}


	abstract protected IHttpClient clientFactory();

	private boolean checkMacBlacklist(String mac) throws Exception{
		boolean b = false;
		IHttpClient masterClient = clientFactory();

		JsonObject jsonObject = masterClient.responseManager(masterClient.get("/public/" + APP_FAMILY_PAGHE + "/blacklist/mac/" + mac));
		b = jsonObject.getBoolean("data");
		if (b) {
			LOGGER.log(Level.WARNING, "The mac address is black-listed");
		} else {
			LOGGER.log(Level.INFO, "The mac address is NOT black-listed");
		}			

		return b;
	}

	@Test
	public void sendTest() throws Exception {
		IHttpClient masterClient = clientFactory();        	
		// Test on Production 

		JsonObject jsonObj1 = masterClient.send(MasterClientAbstract.client);
		assertNotNull(jsonObj1);
		assertTrue(jsonObj1.getInt("data")>=0);
		JsonObject jsonObj2 = masterClient.send(MasterClientAbstract.titolari);
		assertNotNull(jsonObj2);
		assertTrue(jsonObj2.getInt("data")>=0);
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
	public void sendTestOnEchoServer() throws Exception{
		IHttpClient masterClient = clientFactory();
		// Test on Echo server        
		String url1 = HttpClientTest.ECHO_API;
		JsonObject jsonObj = masterClient.send(url1, MasterClientAbstract.client);
		assertNotNull(jsonObj);
	}
	@Test
	public void testMacBlacklist_1() throws Exception{ 	
		boolean b = checkMacBlacklist(MAC_1);
		assertFalse(b);
	}

	@Test
	public void testMacBlacklist_2() throws Exception{ 	
		boolean b = checkMacBlacklist(MAC_2);
		assertFalse(b);
	}    



	@AfterAll
	public static void delFiles(){

	}
}