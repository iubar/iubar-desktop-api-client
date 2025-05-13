package it.iubar.desktop.api;

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

public class MasterClientTest2 {
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		MasterClientAbstract.loadConfig();
	}

	
	protected IHttpClient clientFactory() {
		HmacClient masterClient = null;
		masterClient = new HmacClient();
		// masterClient.loadConfig();
		masterClient.setBaseUrl(RestApiConsts.CRM_BASE_ROUTE);
		return masterClient;
	}

	@Test
	public void sendClient() {
		ClientModel client = ClientModelTest.factory();
		String url = AuthHttpClient.INSERT_CLIENT; // questo è solo un test, potrei tranquillamente invocare  HttpMethods.modelSend(null, client); senza specificare la rotta
		try {
			HttpMethods.modelSend(url, client);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void sendIncrementDocumento() {
		String input = "{" + "\"iddoctype\": \"1\", " + "\"qnt\": \"0\"" + "}";
		try {
			HttpMethods obj = new HttpMethods();
			obj.send(input, AuthHttpClient.INCREMENT_DOC, true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void sendRegisterClientFail() {

		String input = "{" + "\"mac\": \"B8-CA-3A-96-BD-03\", " + "\"cf\": \"DMNLSN95P14D969J\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "/public/register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		try {
			HttpMethods obj = new HttpMethods();
			obj.send(input, path, false);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void sendRegisterClient() {
		String input = "{" + "\"mac\": \"" + ClientModelTest.MAC + "\", " + "\"cf\": \"" + ClientModelTest.CF + "\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "/public/register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		try {
			HttpMethods obj = new HttpMethods();
			obj.send(input, path, true);
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void sendTitolare() {
		TitolareModel titolare = TitolareModelTest.factory();
		ModelsList<TitolareModel> titolari = new ModelsList<TitolareModel>(ClientModelTest.MAC, MasterClientAbstract.ID_APP_PAGHEOPEN, "titolari");
		titolari.add(titolare);		
		try {
			HttpMethods.modlesSend(titolari);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveBlacklist() {
		String path = "/public/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/blacklist/" + "mac/"
				+ ClientModelTest.MAC;
		try {
			HttpMethods obj = new HttpMethods();
			obj.receive(path);
			obj.isDataFalse();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	

	@Test
	public void receiveInfoMac() {
		String path = "/public/paghe/info/mac/" + ClientModelTest.MAC;
		try {
			HttpMethods obj = new HttpMethods();
			obj.receive(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	

	@Test
	public void receiveNow() {
		String path = "/public/now/europe/rome";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receive(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void receiveAggiornamentiMese() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/updated/" + "2017-01-01/" + "2017-05-26";
		try {		
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveAnalisi() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/analytics";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveCedolino() {
		String path = "stats/cedolini/" + "2016-05-06/" + "2017-05-06";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveCountDocumentiMese() {
		String path = "count-documenti/" + "1/" + "2017-08-19/" + "2017-09-19";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInfoTitolare() {
		String path = "info/cognome/" + "Corsini";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInstallazioniMese() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed/" + "2017-01-01/" + "2017-05-26";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveInstallazioni() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveStatisticheGenerali() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/" + "2016-05-06/" + "2017-05-06";
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}	

	@Test
	public void receiveTopUsers() {
		// String path = "top-users?limit=10"; // se volessi usare questa stringa dovrei invocare HttpMethods.dummyTooComplicated()
		String path = "top-users";
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("limit", "10");
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path, queryParams);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void receiveUltimiUtenti() {
		// String path = "last-users?limit=10"; // se volessi usare questa stringa dovrei invocare HttpMethods.dummyTooComplicated()
		String path = "last-users";
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("limit", "10");
		try {
			HttpMethods obj = new HttpMethods();
			obj.receiveProtected(path, queryParams );
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
