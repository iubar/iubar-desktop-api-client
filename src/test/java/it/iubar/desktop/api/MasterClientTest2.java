package it.iubar.desktop.api;

import static org.junit.jupiter.api.Assertions.fail;

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
		ClientModel client = ClientModelTest.factory();
		String url = AuthHttpClient.INSERT_CLIENT; // questo è solo un test, potrei tranquillamente invocare  HttpMethods.modelSend(null, client); senza specificare la rotta
		try {
			HttpMethods.modelSend(url, client);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void sendContatto() {
		CcnlModel contratto = CcnlModelTest.factory();
		ModelsList<CcnlModel> contratti = new ModelsList<CcnlModel>(ClientModelTest.MAC,
				MasterClientAbstract.ID_APP_PAGHEOPEN, "contratti");
		contratti.add(contratto);

		try {
			HttpMethods.modlesSend(contratti);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void sendDatore() {
		DatoreModel datore = DatoreModelTest.factory();
		ModelsList<DatoreModel> datori = new ModelsList<DatoreModel>(ClientModelTest.MAC,
				MasterClientAbstract.ID_APP_PAGHEOPEN, "datori");
		datori.add(datore);

		try {
			HttpMethods.modlesSend(datori);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void sendDoc() {
		DocModel doc = DocModelTest.factory();

		try {
			HttpMethods.modelSend(null, doc);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void sendIncrementDocumento() {
		String input = "{" + "\"iddoctype\": \"1\", " + "\"qnt\": \"0\"" + "}";
		String path = "increment-documento";

		try {
			HttpMethods.send(input, path, true);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void sendRegisterClientFail() {

		String input = "{" + "\"mac\": \"B8-CA-3A-96-BD-03\", " + "\"cf\": \"DMNLSN95P14D969J\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		try {
			HttpMethods.send(input, path, false);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void sendRegisterClient() {
		String input = "{" + "\"mac\": \"" + ClientModelTest.MAC + "\", " + "\"cf\": \"" + ClientModelTest.CF + "\", "
				+ "\"nome\": \"ALESSANDRO\", " + "\"cognome\": \"DAMONTE\"" + "}";
		String path = "register-client/" + Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE);

		try {
			HttpMethods.send(input, path, true);
		} catch (Exception e) {
			fail();
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
			fail();
		}
	}

	@Test
	public void receiveAggiornamentiMese() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/updated/" + "2017-01-01/"
				+ "2017-05-26";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveAnalisi() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/analytics";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveBlacklist() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/blacklist/" + "mac/"
				+ ClientModelTest.MAC;

		try {
			HttpMethods.receive(path);
			HttpMethods.isDataFalse();
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveCedolino() {
		String path = "stats/cedolini/" + "2016-05-06/" + "2017-05-06";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveCountDocumentiMese() {
		String path = "count-documenti/" + "1/" + "2017-08-19/" + "2017-09-19";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveDocumenti() {
		String path = "stats/doc/" + "2016-05-06/" + "2017-05-06";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveElencoProvincie() {
		String path = "provincia/all";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveGreylist() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/greylist/mac/"
				+ ClientModelTest.MAC;

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveInfoMac() {
		String path = "info/" + ClientModelTest.MAC;

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveInfoTitolare() {
		String path = "info/" + "cognome/" + "prova";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveInstallazioniMese() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed/" + "2017-01-01/"
				+ "2017-05-26";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveInstallazioni() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/installed";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveNow() {
		String path = "now/" + "europe/" + "rome";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receivePreventivi() {
		String path = "stats/preventivi/" + "2016-05-06/" + "2017-05-06";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveStatisticheGenerali() {
		String path = Integer.toString(MasterClientAbstract.ID_FAMILY_PAGHE) + "/stats/" + "2016-05-06/" + "2017-05-06";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveTopUsers() {
		String path = "top-users/limit/" + "10";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void receiveUltimiUtenti() {
		String path = "last-users/limit/" + "10";

		try {
			HttpMethods.receive(path);
		} catch (Exception e) {
			fail();
		}
	}
}
