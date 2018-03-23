package it.iubar.desktop.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
 
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.iubar.desktop.api.models.CcnlModel;
import it.iubar.desktop.api.models.DatoreModel;
import it.iubar.desktop.api.models.DocModel;
import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;
import it.iubar.desktop.api.models.TitolareModel;

public abstract class HttpClient {

	private final static Logger LOGGER = Logger.getLogger(HttpClient.class.getName());

	private String user = null;
	private String apiKey = null;
	private boolean isAuth = false;
	private String url = null;


//	private final String IS_AUTH_VALUE = "is_auth";
//	private final String HOST_VALUE = "host";
//	private final String USER_VALUE = "user";
//	private final String API_KEY_VALUE = "api_key";

	public final static String INSERT_CLIENT = "client";
	public final static String INSERT_TITOLARI = "titolari";
	public final static String INSERT_DATORI = "datori";
	public final static String INSERT_CONTRATTI = "contratti";
	public final static String INSERT_DOCUMENTI = "documenti-mese";
	public final static String INSERT_MAC = "list/mac";

	abstract protected JSONObject genAuth2(String destUrl);
	abstract public Response get(String restUrl);
	
//	public void loadConfigFromFile(String cfgFile) throws IOException {
//		File file = new File(cfgFile);
//		InputStream is = new FileInputStream(file);
//		this.setUpIni(is);
//	}


//	public void loadConfigFromJar() {
//		// Soluzione 1
//		ClassLoader classLoader = getClass().getClassLoader();
//		File file = new File(classLoader.getResource("config.ini").getFile());
//		// Soluzione 2
//		// NO: File file = new File("src/main/resources/config.ini");
//		// Soluzione 3
//		InputStream is = getClass().getResourceAsStream("/config.ini");
//		// eg: BufferedReader reader = new BufferedReader(new
//		// InputStreamReader(in));
//
//		this.setUpIni(is);
//	}

	// Sets all the variables looking at the ".ini" file.
//	private void setUpIni(InputStream inputStream) {
//		Properties properties = new Properties();
//		try {
//			properties.load(inputStream);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		String host = properties.getProperty(HOST_VALUE);
//		String auth = properties.getProperty(IS_AUTH_VALUE, "false");
//		this.setAuth(fromStringToBool(auth));
//		this.setBaseUrl(host);
//
//		if (isAuth()) {
//			String apiKey = properties.getProperty(API_KEY_VALUE);
//			String user = properties.getProperty(USER_VALUE);
//			this.setUser(user);
//			this.setApiKey(apiKey);
//		}
//		LOGGER.log(Level.FINE, "Config file parsed succesfully");
//
//	}

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

	public JSONObject send(String destUrl, IJsonModel obj) throws Exception {
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
		JSONObject answer = null;
 if(response!=null) {
		int status = response.getStatus();
		String output = response.readEntity(String.class);
		answer = new JSONObject(output);
		System.out.println("Response status: " + status);
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
 }else {
		String msg = "Response is null";;
		LOGGER.log(Level.SEVERE, msg);
		throw new Exception(msg);
 }
		return answer;
	}
 


	public HttpClient() {
		super();
	}

	protected String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	protected String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	protected boolean isAuth() {
		return this.isAuth;
	}

	public void setAuth(boolean auth) {
		this.isAuth = auth;
	}

	public String getBaseUrl() {
		return this.url;
	}

	public void setBaseUrl(String url) {
		this.url = url;
	}

	private JSONObject genAuth(String destUrl, JSONObject jsonObj) {
		JSONObject authData = genAuth2(destUrl);
		if (jsonObj != null) {
			authData.put("data", jsonObj);
		}
		return authData;
	}

	private JSONObject genAuth(String destUrl, JSONArray jsonArray) {
		JSONObject authData = genAuth2(destUrl);
		if (jsonArray != null) {
			authData.put("data", jsonArray);
		}
		return authData;
	}

 

	/**
	 * Il metodo implementa la funzione PHP rawurlencode()
	 * 
	 * @see: http://php.net/manual/en/function.rawurlencode.php
	 * @param string
	 * @return string
	 */
	protected String rawUrlEncode(String string) { // All non-ascii characters in URL has to be 'x-url-encoding' encoded.
		//String encoded = string.replaceAll("\\+", "%2B"); // analogo a rawurlencode di Php
		//encoded = encoded.replaceAll(":", "%3A"); // analogo a rawurlencode di Php
		String encoded = null;
		try {
			encoded = URLEncoder.encode(string, "UTF-8"); // converts a String to the application/x-www-form-urlencoded MIME format.
			encoded = encoded.replace("+", "%20"); // converts all space chars (the plus char in a mime encoded string) to "%20" like the rawurlencode() function in Php
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}			
		return encoded;
	}

	public Response post(String restUrl, final JSONArray data) {
		Entity<String> d3 = null;
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			JSONObject data2 = genAuth(restUrl, data);
			String str = data2.toString();
			d3 = Entity.json(str);
		} else {
			d3 = Entity.json(data.toString());			
		}
		return post(restUrl, d3);
	}

	public Response post(String restUrl, JSONObject data) {
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			data = genAuth(restUrl, data);
		}
		Entity<String> d1 = Entity.text(data.toString());
		Entity<String> d2 = Entity.entity(data.toString(), MediaType.APPLICATION_JSON);
		Entity<String> d3 = Entity.json(data.toString()); // See: https://jersey.java.net/documentation/latest/client.html#d0e4692		
// Se volessi utilizzare il post di tipo "application/x-www-form-urlencoded"	
//		Form form = new Form();
//	    form.param("user", "XXX");
//	    Entity d4 = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED);

		return post(restUrl, d3);
	}

	public Response post(String restUrl, Entity<String> d3) {
		System.out.println("POST: " + restUrl);
		Client client = HttpClient.newClient();
		WebTarget target = client.target(restUrl);
		// Accetto risposte di tipo Json
		Response response = target.request(MediaType.APPLICATION_JSON).accept("application/json").header("X-Requested-With", "XMLHttpRequest").post(d3);	
		return response;
	}

	public static Client newClient() {
		ClientConfig configuration = new ClientConfig();
		configuration.property(ClientProperties.CONNECT_TIMEOUT, 1500);
		configuration.property(ClientProperties.READ_TIMEOUT, 3000);
		Client client = ClientBuilder.newClient(configuration);
		return client;
	}

	/*
	 * Il seguente metodo implementa la logica di risoluzione delle rotte
	 * dettata dall'RFC3986, ovvero la stessa utilizzata da Guzzle e descritta
	 * nel manuale dello stesso progetto
	 * 
	 * @see: http://docs.guzzlephp.org/en/5.3/clients.html
	 */
	protected String resolveUrl(String restUrl) {
		String baseUrl = this.getBaseUrl();
		if (!restUrl.startsWith("http")) {
			if (!isAbsoluteRoute(restUrl)) {
				String lastChar = baseUrl.substring(baseUrl.length() - 1);
				if (lastChar.equals("/")) {
					restUrl = baseUrl + restUrl;
				} else {
					restUrl = getRootUrl(baseUrl) + "/" + restUrl;
				}
			} else {
				restUrl = getRootUrl(baseUrl) + restUrl;
			}
		} else {
			// la rotta è nel formato "http://..."
		}
		return restUrl;
	}

	private boolean isAbsoluteRoute(String restUrl) {
		String s = restUrl.substring(0, 1);
		if (s.equals("/")) {
			return true;
		}
		return false;
	}

	private String getRootUrl(String strUrl) {
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String host = url.getHost();
		String protocol = url.getProtocol();
		int port = url.getPort();
		String baseUrl = protocol + "://" + host + ":" + port;
		return baseUrl;
	}


	protected JSONObject genAuth(String destUrl) {
		JSONObject authData = genAuth2(destUrl);
		return authData;
	}



}
