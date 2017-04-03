package it.iubar.desktop.api;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterClient extends MasterClient0 {

	private final static Logger LOGGER = Logger.getLogger(MasterClient.class.getName());

	public static final String ROUTE_BASE = "http://www.iubar.it/crm/api/crm/v1/";

	private final String IS_AUTH_VALUE = "is_auth";
	private final String HOST_VALUE = "host";
	private final String USER_VALUE = "user";
	private final String API_KEY_VALUE = "api_key";

	public final static String INSERT_CLIENT = "client";
	public final static String INSERT_TITOLARI = "titolari";
	public final static String INSERT_DATORI = "datori";
	public final static String INSERT_CONTRATTI = "contratti";
	public final static String INSERT_DOCUMENTI = "documenti-mese";
	public final static String INSERT_MAC = "list/mac";

	public void loadConfigFromFile(String cfgFile) throws IOException {
		File file = new File(cfgFile);
		InputStream is = new FileInputStream(file);
		this.setUpIni(is);
	}

	public MasterClient() {
		super();
	}

	public void loadConfigFromJar() {
		// Soluzione 1
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("config.ini").getFile());
		// Soluzione 2
		// NO: File file = new File("src/main/resources/config.ini");
		// Soluzione 3
		InputStream is = getClass().getResourceAsStream("/config.ini");
		// eg: BufferedReader reader = new BufferedReader(new
		// InputStreamReader(in));

		this.setUpIni(is);
	}

	// Sets all the variables looking at the ".ini" file.
	private void setUpIni(InputStream inputStream) {
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String host = properties.getProperty(HOST_VALUE);
		String auth = properties.getProperty(IS_AUTH_VALUE, "false");
		this.setAuth(fromStringToBool(auth));
		this.setBaseUrl(host);

		if (isAuth()) {
			String apiKey = properties.getProperty(API_KEY_VALUE);
			String user = properties.getProperty(USER_VALUE);
			this.setUser(user);
			this.setApiKey(apiKey);
		}
		LOGGER.log(Level.FINE, "Config file parsed succesfully");

	}

	private boolean fromStringToBool(String s) {
		return s.equalsIgnoreCase("true");
	}

	private String normalizePath(String path) {
		String finalPath = path;
		if (!finalPath.equalsIgnoreCase("") && !finalPath.substring(0, 1).equalsIgnoreCase("/")) {
			finalPath = "/" + finalPath;
		}

		return finalPath;
	}

	public <T> JSONObject send(IJsonModel obj) throws Exception {
		return send(getRoute(obj), obj);
	}

	public <T> JSONObject send(ModelsList<? super T> docModellist) throws Exception {
		return send(getRoute(docModellist), docModellist);
	}

	private <T> JSONObject send(String destUrl, ModelsList<? super T> docModellist) throws Exception {
		JSONObject dataToSend = new JSONObject();
		dataToSend.putOnce("mac", docModellist.getMac());
		Object idApp = docModellist.getIdApp();
		if (idApp != null) {
			dataToSend.putOnce("idapp", idApp);
		}
		dataToSend.putOnce(docModellist.getJsonName(), docModellist.getJsonArray());
		Response response = post(destUrl, dataToSend);
		JSONObject jsonObj = responseManager(response);
		return jsonObj;
	}

	protected JSONObject send(String destUrl, IJsonModel obj) throws Exception {
		cantBeNull(obj);
		JSONObject dataToSend = null;
		String json = obj.asJson();
		dataToSend = new JSONObject(json);
		Response response = post(destUrl, dataToSend);
		JSONObject jsonObj = responseManager(response);
		return jsonObj;
	}

	private <T> boolean isNull(T obj) {
		return obj == null;
	}

	private <T> void cantBeNull(T obj) {
		if (isNull(obj)) {
			LOGGER.log(Level.SEVERE, "Object to send cannot be nullable.");
			throw new RuntimeException("Object to send cannot be nullable.");
		}
	}

	private <T> String getRoute(T obj) {
		String urlToSend = this.getBaseUrl();
		String lastChar = urlToSend.substring(urlToSend.length() - 1);
		if (!lastChar.equals("/")) {
			urlToSend = urlToSend + "/";
		}
		if (obj instanceof it.iubar.desktop.api.models.ClientModel) {
			urlToSend += INSERT_CLIENT;
		} else if (obj instanceof it.iubar.desktop.api.models.DocModel) {
			urlToSend += INSERT_DOCUMENTI;
		} else if (obj instanceof ModelsList) {
			ModelsList ml = ((ModelsList) obj);
			if (ml.getSize() > 0) {
				Class c = ml.getElemClass();
				if (c.equals(DatoreModel.class)) {
					urlToSend += INSERT_DATORI;
				} else if (c.equals(TitolareModel.class)) {
					urlToSend += INSERT_TITOLARI;
				} else if (c.equals(CcnlModel.class)) {
					urlToSend += INSERT_CONTRATTI;
				} else if (c.equals(DocModel.class)) {
					urlToSend += INSERT_DOCUMENTI;
				} else {
					throw new RuntimeException("Situazione imprevista");
				}
			} else {
				throw new RuntimeException("Situazione imprevista");
			}
		} else {
			throw new RuntimeException("Situazione imprevista");
		}

		return urlToSend;
	}

	@Deprecated
	// Spaghetti code
	public JSONObject responseManager(Response response) throws Exception {

		int status = response.getStatus();
		String output = response.readEntity(String.class);
		JSONObject answer = new JSONObject(output);

		System.out.println("Response status: " + response.getStatus());
		System.out.println("Response data: " + output);

		if (status == 201 || status == 200) {

			String resp = "";
			if (answer.has("response")) {
				resp = answer.getString("response");
			}

			try {
				String msg = "Query ok, code: " + status + ", rows affected: " + resp;
				LOGGER.log(Level.FINE, msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (status == 400) {
			String msg = "Bad request, code: " + status + ", output: " + output;
			LOGGER.log(Level.SEVERE, msg);
			throw new Exception(msg);
		} else if (status == 404) {
			String msg = "Not found, code: " + status + ", output: " + output;
			LOGGER.log(Level.SEVERE, msg);
			throw new Exception(msg);
		} else if (status == 500) {
			String msg = "Internal server error, code: " + status + ", output: " + output;
			LOGGER.log(Level.SEVERE, msg);
			throw new Exception(msg);
		} else {
			String msg = "Unknown error, code: " + status + ", output: " + output;
			LOGGER.log(Level.SEVERE, msg);
			throw new Exception(msg);
		}
		return answer;
	}

}
