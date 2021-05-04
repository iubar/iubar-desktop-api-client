package it.iubar.desktop.api;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;

import it.iubar.desktop.api.json.JsonUtils;
import it.iubar.desktop.api.models.CcnlModel;
import it.iubar.desktop.api.models.DatoreModel;
import it.iubar.desktop.api.models.DocModel;
import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;
import it.iubar.desktop.api.models.TitolareModel;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public abstract class AuthHttpClient extends HttpClient {

	private static final Logger LOGGER = Logger.getLogger(AuthHttpClient.class.getName());

	private String user = null;
	private String apiKey = null;

	private boolean isAuth = false;

	public final static String INSERT_CLIENT = "client";
	public final static String INSERT_TITOLARI = "titolari";
	public final static String INSERT_DATORI = "datori";
	public final static String INSERT_CONTRATTI = "contratti";
	public final static String INSERT_DOCUMENTI = "documenti-mese";
	public final static String INSERT_MAC = "list/mac";

	abstract protected JsonObject genAuth2(String destUrl);

	abstract public Response get(String restUrl);

	public <T> JsonObject send(IJsonModel obj) throws Exception {
		return send(getRoute(obj), obj);
	}

	public <T> JsonObject send(ModelsList<? super T> docModellist) throws Exception {
		return send(getRoute(docModellist), docModellist);
	}

	private <T> JsonObject send(String destUrl, ModelsList<? super T> docModellist) throws Exception {	
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();		
		objectBuilder.add("mac", docModellist.getMac());
		objectBuilder.add("idapp", docModellist.getIdApp());
		objectBuilder.add(docModellist.getJsonName(), docModellist.getJsonArray());		  
		JsonObject dataToSend = objectBuilder.build();
		LOGGER.log(Level.INFO, dataToSend.toString());
		Response response = post(destUrl, dataToSend);
		JsonObject jsonObj = responseManager(response);

		return jsonObj;
	}

	public JsonObject send(String destUrl, IJsonModel obj) throws Exception {
		cantBeNull(obj);
		JsonObject dataToSend = null;
		String json = obj.asJson();
		dataToSend = JsonUtils.fromString(json);
		LOGGER.log(Level.INFO, dataToSend.toString());
		Response response = post(destUrl, dataToSend);
		JsonObject jsonObj = responseManager(response);

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

	private <T> String getRoute(T obj) throws Exception {
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
				throw new RuntimeException("Situazione imprevista per classe " + ml.getClass().getCanonicalName());
			}
		} else {
			throw new RuntimeException("Situazione imprevista per classe " + obj.getClass().getCanonicalName());
		}

		return urlToSend;
	}

	@Deprecated
	// Spaghetti code
	// forse c'è anche codice duplicato dal metodo HttpClient.getAnswer()
	public JsonObject responseManager(Response response) throws Exception {
		JsonObject answer = null;
		if (response != null) {
			int status = response.getStatus();
			String output = response.readEntity(String.class);
			answer = JsonUtils.parseJsonString(output);

			LOGGER.log(Level.INFO, "Response status: " + status);
			LOGGER.log(Level.INFO, "Response data: " + output);

			if (status == 201 || status == 200) {
				String resp = "";
				if (answer.get("response")!=null) {
					resp = answer.getString("response");
				}

				String msg = "Query ok, code: " + status + ", rows affected: " + resp;
				LOGGER.log(Level.FINE, msg);

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
		} else {
			String msg = "Response is null";
			LOGGER.log(Level.SEVERE, msg);
			throw new Exception(msg);
		}
		return answer;
	}

	public AuthHttpClient() {
		super();
	}

	protected String getUser() {
		return this.user;
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

	private JsonObject genAuth(String destUrl, JsonObject jsonObj) {
		JsonObject authData = genAuth2(destUrl);
		if (jsonObj != null) {
			authData.put("data", jsonObj);
		}
		return authData;
	}

	private JsonObject genAuth(String destUrl, JsonArray jsonArray) {
		JsonObject authData = genAuth2(destUrl);
		if (jsonArray != null) {
			authData.put("data", jsonArray);
		}
		return authData;
	}

	protected JsonObject genAuth(String destUrl) {
		JsonObject authData = genAuth2(destUrl);
		return authData;
	}

	/**
	 * Il metodo implementa la funzione PHP rawurlencode()
	 * 
	 * @see: http://php.net/manual/en/function.rawurlencode.php
	 * @param string
	 * @return string
	 */
	protected String rawUrlEncode(String string) { // All non-ascii characters in URL has to be 'x-url-encoding'
													// encoded.
		// String encoded = string.replaceAll("\\+", "%2B"); // analogo a rawurlencode
		// di Php
		// encoded = encoded.replaceAll(":", "%3A"); // analogo a rawurlencode di Php
		String encoded = null;
		try {
			encoded = URLEncoder.encode(string, "UTF-8"); // converts a String to the application/x-www-form-urlencoded
															// MIME format.
			encoded = encoded.replace("+", "%20"); // converts all space chars (the plus char in a mime encoded string)
													// to "%20" like the rawurlencode() function in Php
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoded;
	}

	public Response post(String restUrl, final JsonArray data) {
		Entity<String> d3 = null;
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			JsonObject data2 = genAuth(restUrl, data);
			String str = data2.toString();
			d3 = Entity.json(str);
		} else {
			d3 = Entity.json(data.toString());
		}
		return post(restUrl, d3);
	}

	public Response post(String restUrl, JsonObject data) {
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			data = genAuth(restUrl, data);
		}
		Entity<String> d1 = Entity.text(data.toString());
		Entity<String> d2 = Entity.entity(data.toString(), MediaType.APPLICATION_JSON);
		Entity<String> d3 = Entity.json(data.toString()); // See:
															// https://jersey.java.net/documentation/latest/client.html#d0e4692
		// Se volessi utilizzare il post di tipo "application/x-www-form-urlencoded"
		// Form form = new Form();
		// form.param("user", "XXX");
		// Entity d4 = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED);

		return post(restUrl, d3);
	}

	public Response post(String restUrl, Entity<String> d3) {
		LOGGER.log(Level.INFO, "POST: " + restUrl);
		Client client = HttpClient.newClient();
		WebTarget target = client.target(restUrl);
		// Accetto risposte di tipo Json
		Response response = null;
		try {
			response = target.request(MediaType.APPLICATION_JSON).accept("application/json")
					.header("X-Requested-With", "XMLHttpRequest").post(d3);
		} catch (ProcessingException e) {
			String msg = e.getMessage();
			LOGGER.log(Level.SEVERE, msg, e);
			if (msg.indexOf("connect timed out") > -1) {
				Configuration config = client.getConfiguration();
				Object connTimeout = config.getProperty(ClientProperties.CONNECT_TIMEOUT);
				Object readTimeout = config.getProperty(ClientProperties.READ_TIMEOUT);
				LOGGER.log(Level.CONFIG, "CONNECT_TIMEOUT: " + String.valueOf(connTimeout));
				LOGGER.log(Level.CONFIG, "READ_TIMEOUT: " + String.valueOf(readTimeout));
			}
		}
		return response;
	}

	/*
	 * Il seguente metodo implementa la logica di risoluzione delle rotte dettata
	 * dall'RFC3986, ovvero la stessa utilizzata da Guzzle e descritta nel manuale
	 * dello stesso progetto
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
		String baseUrl = null;
		URL url = null;
		try {
			url = new URL(strUrl);
			baseUrl = url.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return baseUrl;
	}

}
