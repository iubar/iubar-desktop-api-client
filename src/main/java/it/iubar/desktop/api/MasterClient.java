package it.iubar.desktop.api;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;
import it.iubar.desktop.api.services.JSONPrinter;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterClient {

	private final static Logger LOGGER = Logger.getLogger(MasterClient.class
			.getName());

	private final String IS_AUTH_VALUE = "is_auth";
	private final String HOST_VALUE = "host";
	private final String PATH_VALUE = "path";
	private final String USER_VALUE = "user";
	private final String API_KEY_VALUE = "api_key";

	public final static String INSERT_CLIENT = "/client";
	public final static String INSERT_TITOLARE = "/titolari";
	public final static String INSERT_DATORE = "/datore";
	public final static String INSERT_CONTRATTO = "/contratto";
	public final static String INSERT_DOCUMENTO = "/documento-mese";
	public final static String INSERT_TITOLARI = "/titolare";
	public final static String INSERT_DATORI = "/datori";
	public final static String INSERT_CONTRATTI = "/contratti";
	public final static String INSERT_DOCUMENTI = "/documenti-mese";
	public final static String INSERT_MAC = "/list/mac";

	private String user;
	private String apiKey;
	private boolean isAuth;
	private String url;

	public void loadConfigFromFile(String cfgFile) throws IOException {
		File file = new File(cfgFile);
		InputStream is = new FileInputStream(file);
		this.setUpIni(is);
	}

	public MasterClient() throws IOException {
		super();
	}
	
	public void loadConfig(){
		// Soluzione 1		
		ClassLoader classLoader = getClass().getClassLoader();
	    File file = new File(classLoader.getResource("config.ini").getFile());  
	    // Soluzione 2	    
	    //		NO: File file = new File("src/main/resources/config.ini");
	    // Soluzione 3		
		InputStream is = getClass().getResourceAsStream("/config.ini"); 
		// eg: BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		this.setUpIni(is);		
	}

	private String getUser() {
		return user;
	}

	void setUser(String user) {
		this.user = user;
	}

	private String getApiKey() {
		return apiKey;
	}

	void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	private boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean auth) {
		isAuth = auth;
	}

	public String getBaseUrl() {
		return url;
	}

	public void setBaseUrl(String url) {
		this.url = url;
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

		String user = null;
		String apiKey = null;
		String host = null;

		String auth = properties.getProperty(IS_AUTH_VALUE, "false");
		
		if (fromStringToBool(auth)) {
			apiKey = properties.getProperty(API_KEY_VALUE);
			user = properties.getProperty(USER_VALUE);
			this.setUser(user);
			this.setApiKey(apiKey);
		}
		host = properties.getProperty(HOST_VALUE);

 
		String path = properties.getProperty(PATH_VALUE, "/");

		LOGGER.log(Level.FINE, "Config file parsed succesfully");

		this.setAuth(fromStringToBool(auth));

		this.setBaseUrl(host + path);
	}

	private boolean fromStringToBool(String s) {
		return s.equalsIgnoreCase("true");
	}

	private String normalizePath(String path) {
		String finalPath = path;
		if (!finalPath.equalsIgnoreCase("")
				&& !finalPath.substring(0, 1).equalsIgnoreCase("/")){
			finalPath = "/" + finalPath;
		}

		return finalPath;
	}
 
	public Response send(IJsonModel obj) throws Exception {
		return send(getRoute(obj), obj);
	}

	public Response send(ModelsList docModellist) throws Exception {
		return send(getRoute(docModellist), docModellist);
	}

	public Response send(String destUrl, ModelsList docModellist) throws Exception {
		JSONObject dataToSend = new JSONObject();
		dataToSend.putOnce("mac", docModellist.getMac());
		dataToSend.putOnce(docModellist.getJsonName(), docModellist.getJsonArray());
		return send2(destUrl, dataToSend);
	}

	public Response send(String destUrl, IJsonModel obj) throws Exception {
		cantBeNull(obj);
		JSONObject dataToSend = null;
		try {
			String json = JSONPrinter.toJson(obj);
			dataToSend = new JSONObject(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "Jackson could not convert the object correctly.", JsonProcessingException.class);
			throw new RuntimeException("Jackson could not convert the object correctly.");
		}
		return send2(destUrl, dataToSend);
	}

	private Response send2(String destUrl, JSONObject dataToSend) throws Exception {
		if (this.isAuth()) {
			dataToSend = genAuth(destUrl, dataToSend);
		}
		Response response = post(destUrl, dataToSend);
		responseManager(response);
		return response;
	}

	private JSONObject genAuth(String destUrl, JSONObject toJson) {
		JSONObject authData = new JSONObject();
		authData.put(USER_VALUE, this.getUser());
		authData.put("timestamp", getTimeStamp());
		authData.put("data", toJson);
		authData.put("signature", encryptedData(destUrl, toJson.toString()));
		return authData;
	}

	private Response post(String restUrl, JSONObject data) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(restUrl);
		Response response = target
				.request(MediaType.APPLICATION_JSON)
				.accept("application/json")
				.header("X-Requested-With", "XMLHttpRequest")
				.post(Entity.text(data.toString()));
		return response;				
	}

	private Response get(String restUrl) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(restUrl);	
		Response response = target
				.request(MediaType.APPLICATION_JSON)
				.accept("application/json")
				.header("X-Requested-With", "XMLHttpRequest")
				.get();		
		return response;
	}

	public ListMac checkMac(String macAddress) throws Exception {
		cantBeNull(macAddress);		
		String restUrl = this.getBaseUrl() + INSERT_MAC + "/" + macAddress;
		JSONObject jsonObject = responseManager(this.get(restUrl));
		if (jsonObject.getBoolean("black-list")) {
			return new ListMac(true);
		} else {
			JSONObject greyList = jsonObject.getJSONObject("grey-list");
			return new ListMac(greyList.getInt("idreason"),greyList.getString("desc"));
		}
	}

	private String encryptedData(String destUrl, String data) {
		String payload = destUrl + this.getUser() + this.getTimeStamp() + this.getApiKey() + data;
		String algo = "HmacSHA256";
		String keyString = this.getApiKey();
		if(keyString==null || keyString.equals("")){
			throw new RuntimeException("apikey is null or empty");
		}
		try {
			Mac sha256_HMAC = Mac.getInstance(algo);
			SecretKeySpec secret_key = new SecretKeySpec(keyString.getBytes(), algo);
			sha256_HMAC.init(secret_key);
			return Base64.encodeBase64String(sha256_HMAC.doFinal(payload.getBytes()));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unable to generate encypted data.");
			throw new RuntimeException("Unable to generate encypted data.");
		}
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
				.format(new Date());
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

		if (obj instanceof it.iubar.desktop.api.models.ClientModel) {
			urlToSend += INSERT_CLIENT;
		} else if (obj instanceof DatoreModel) {
			urlToSend += INSERT_DATORE;
		} else if (obj instanceof TitolareModel) {
			urlToSend += INSERT_TITOLARE;
		} else if (obj instanceof CcnlModel) {
			urlToSend += INSERT_CONTRATTO;
		} else if (obj instanceof DocModel) {
			urlToSend += INSERT_DOCUMENTO;
		} else if (obj instanceof ModelsList) {
			Class c = ((ModelsList) obj).getElemClass();
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

		return urlToSend;
	}

	private JSONObject responseManager(Response response) throws Exception {

		int status = response.getStatus();

		if (status == 201 || status == 200) {
			JSONObject answer = new JSONObject(response.readEntity(String.class));
			try {
				LOGGER.log(Level.FINE, "Query ok, code: " + status+ ", rows affected: " + answer.getString("response"));
			} catch (JSONException e) {
				LOGGER.log(Level.FINE, "Query ok, code: " + status);
			}
			return answer;
		} else if (status == 400) {
			LOGGER.log(Level.SEVERE, "Bad request, code: " + status);
			throw new Exception("Bad request, code: " + status);
		} else if (status == 404) {
			LOGGER.log(Level.SEVERE, "Not found, code: " + status);
			throw new Exception("Not found, code: " + status);
		} else if (status == 500) {
			LOGGER.log(Level.SEVERE, "Internal server error, code: " + status);
			throw new Exception("Internal server error, code: " + status);
		} else {
			LOGGER.log(Level.SEVERE, "Unknown error, code: " + status);
			throw new Exception("Unknown error, code: " + status);
		}

	}

}
