package it.iubar.desktop.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.iubar.desktop.api.models.CcnlModel;
import it.iubar.desktop.api.models.CcnlModelTest;
import it.iubar.desktop.api.models.ClientModel;
import it.iubar.desktop.api.models.ClientModelTest;
import it.iubar.desktop.api.models.DatoreModel;
import it.iubar.desktop.api.models.DatoreModelTest;
import it.iubar.desktop.api.models.DocModel;
import it.iubar.desktop.api.models.DocModelTest;
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

		String url = "http://iubar.it/crm/api/crm/v1/client";
		ClientModel client = ClientModelTest.factory();

		HttpMethods.modelSend(url, client);
	}

	@Test
	public void sendContatto() {

		CcnlModel contratto = CcnlModelTest.factory();
		ModelsList<CcnlModel> contratti = new ModelsList<CcnlModel>(ClientModelTest.MAC,
				MasterClientAbstract.ID_APP_PAGHEOPEN, "contratti");
		contratti.add(contratto);

		HttpMethods.modlesSend(contratti);
	}

	@Test
	public void sendDatore() {
		DatoreModel datore = DatoreModelTest.factory();
		ModelsList<DatoreModel> datori = new ModelsList<DatoreModel>(ClientModelTest.MAC,
				MasterClientAbstract.ID_APP_PAGHEOPEN, "datori");
		datori.add(datore);
		
		HttpMethods.modlesSend(datori);
	}

	//@Test
	public void sendDoc() {
			DocModel doc = DocModelTest.factory();

			HttpMethods.modelSend(null, doc);			
	}

	@Test
	public void sendIncrementDocumento() {
		String input = "{" + "\"iddoctype\": \"1\", " + "\"qnt\": \"0\"" + "}";
		String path = "increment-documento";

		HttpMethods.send(input, path, true);
	}

	@Test
	public void sendRegisterClientFail() {

		String input = "{" + "\"mac\": \"B8-CA-3A-96-BD-03\", " + "\"cf\": \"DMNLSN95P14D969J\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		HttpMethods.send(input, path, false);
	}

	@Test
	public void sendRegisterClient() {
		String input = "{" + "\"mac\": \"" + ClientModelTest.MAC + "\", " + "\"cf\": \"" + ClientModelTest.CF + "\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		HttpMethods.send(input, path, true);

	}

	@Test
	public void sendTitolare() {
		HmacClient masterClient = (HmacClient) clientFactory();
		try {
			masterClient.setAuth(false);
			TitolareModel titolare = TitolareModelTest.factory();

			ModelsList<TitolareModel> titolari = new ModelsList<TitolareModel>(ClientModelTest.MAC,
					MasterClientAbstract.ID_APP_PAGHEOPEN, "titolari");
			titolari.add(titolare);

			JSONObject jsonObjTitolari = masterClient.send(titolari);
			boolean data = jsonObjTitolari.getBoolean("data");

			assertNotNull(jsonObjTitolari);
			assertTrue(data);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void receiveAggiornamentiMese() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/updated/" + "2017-01-01/"
				+ "2017-05-26";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveAnalisi() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/analytics";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveBlacklist() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/blacklist/" + "mac/"
				+ ClientModelTest.MAC;

		HttpMethods.receive(path);
		HttpMethods.isDataFalse();
	}

	@Test
	public void receiveCedolino() {
		String path = "stats/cedolini/" + "2016-05-06/" + "2017-05-06";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveCountDocumentiMese() {
		String path = "count-documenti/" + "1/" + "2017-08-19/" + "2017-09-19";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveDocumenti() {
		String path = "stats/doc/" + "2016-05-06/" + "2017-05-06";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveElencoProvincie() {
		String path = "provincia/all";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveGreylist() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/greylist/" + "mac/"
				+ ClientModelTest.MAC;

		HttpMethods.receive(path);
	}

	@Test
	public void receiveInfoMac() {
		String path = "info/" + ClientModelTest.MAC;

		HttpMethods.receive(path);
	}

	@Test
	public void receiveInfoTitolare() {
		String path = "info/" + "cognome/" + "prova";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveInstallazioniMese() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed/" + "2017-01-01/"
				+ "2017-05-26";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveInstallazioni() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveNow() {
		String path = "now/" + "europe/" + "rome";

		HttpMethods.receive(path);
	}

	@Test
	public void receivePreventivi() {
		String path = "stats/preventivi/" + "2016-05-06/" + "2017-05-06";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveStatisticheGenerali() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/" + "2016-05-06/" + "2017-05-06";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveTopUsers() {
		String path = "top-users/limit/" + "10";

		HttpMethods.receive(path);
	}

	@Test
	public void receiveUltimiUtenti() {
		String path = "last-users/limit/" + "10";

		HttpMethods.receive(path);
	}
}
