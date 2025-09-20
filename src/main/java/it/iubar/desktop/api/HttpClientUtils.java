package it.iubar.desktop.api;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import it.iubar.desktop.api.json.JsonUtils;
import it.iubar.desktop.api.models.ClientModel;
import it.iubar.desktop.api.models.HrModel;
import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;
import it.iubar.desktop.api.models.TitolareModel;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public abstract class HttpClientUtils {

	private static final Logger LOGGER = Logger.getLogger(HttpClientUtils.class.getName());
 	protected String url = null;
 
 
	/**
	 * JAX-RS 2.1+ (Java EE 8 / Jakarta EE 8+)
	 */
	public static Client newClient() {
		Client client = null;
 		//SSLContext sslContext = factorySslContext();
 		// SSLContext sslContext = factoryFakeSslContext();
		//if (sslContext != null) {
			// Costruisci il client JAX-RS con il SSLContext custom
			client = ClientBuilder.newBuilder()
					// .property("jersey.config.client.followRedirects", true)
					// Se ometti .sslContext(...), il client JAX-RS (come Jersey o RESTEasy) userà il SSLContext di default fornito dalla JVM, che di solito è più che sufficiente per la maggior parte dei casi, soprattutto con certificati validi su internet.					
					//.sslContext(sslContext)
					//.hostnameVerifier((hostname, session) -> true) // opzionale: ignora verifica host
					.connectTimeout(3, TimeUnit.SECONDS) // Timeout di connessione
					.readTimeout(5, TimeUnit.SECONDS) // Timeout di lettura
					.build();
 

		//}
		return client;
	}

	/**
	 * Crea il client con autenticazione user/password 
	 * 
	 */
	public static Client newClientProtected(String user, String password) {
		Client client = newClient();
		client.register(HttpAuthenticationFeature.basic(user, password));
		return client;
	}

	private static SSLContext factorySslContext() {
		// Crea un SSLContext con la versione desiderata (es. TLSv1.3 o TLSv1.2)
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, null, null); // Usa truststore/keystore di default
			

            // Imposta i protocolli TLS supportati (forza TLSv1.3)
            SSLParameters sslParams = sslContext.getDefaultSSLParameters();
            sslParams.setProtocols(new String[] { "TLSv1.2" });
            
		} catch (NoSuchAlgorithmException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		} catch (KeyManagementException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
		return sslContext;
	}
 
	/**
	 * ignora la validità del certificato SSL
	 */
	private static SSLContext factoryFakeSslContext() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
		};

		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.severe("ERRORE: " + e.getMessage());
		} catch (KeyManagementException e) {
			LOGGER.severe("ERRORE: " + e.getMessage());
		}
		return sslContext;
	}

	public static JsonObject getAnswer(Response response) {
		JsonObject answer = null;
		if (response != null) {
			String output = response.readEntity(String.class);
			try {
				answer = it.iubar.desktop.api.json.JsonUtils.parseJsonString(output);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return answer;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	public final static String INSERT_HR = "/public/hr";
	public final static String INSERT_CLIENT = "/public/client";
	public final static String INSERT_TITOLARI = "/public/titolari";
	public static final String INCREMENT_DOC = "/public/increment-documento";


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
		Response response = doPost(destUrl, dataToSend);
		JsonObject jsonObj = ResponseUtils.readAsStringToJson(response, true);
		return jsonObj;
	}

	public JsonObject send(String destUrl, IJsonModel obj) throws Exception {
		cantBeNull(obj);
		String json = obj.asJson();
		JsonObject dataToSend = JsonUtils.fromString(json);
		Response response = doPost(destUrl, dataToSend);
		JsonObject jsonObj = ResponseUtils.readAsStringToJson(response, true);
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
		if (obj instanceof ClientModel) {
			urlToSend += INSERT_CLIENT;
		}else if (obj instanceof HrModel) {
			urlToSend += INSERT_HR;
		} else if (obj instanceof ModelsList) {
			ModelsList ml = ((ModelsList) obj);
			if (ml.getSize() > 0) {
				Class c = ml.getElemClass();
				if (c.equals(TitolareModel.class)) {
					urlToSend += INSERT_TITOLARI;
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
	// forse c'è anche codice duplicato dal metodo HttpClientUtils.getAnswer()
	public JsonObject responseManager(Response response) throws Exception {
		JsonObject answer = null;
		if (response != null) {
			int status = response.getStatus();
			String output = response.readEntity(String.class);
			answer = JsonUtils.parseJsonString(output);

			LOGGER.log(Level.INFO, "Response status: " + status);
			LOGGER.log(Level.INFO, "Response data: " + output);

			if (status == 201 || status == 200) { // TODO: usare costanti HttpURLConnection.HTTP_XX
				String msg = "Query ok, code: " + status;
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



	public String getBaseUrl() {
		return this.url;
	}

	public void setBaseUrl(String url) {
		this.url = url;
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

 
	// Entity<String> d3 = Entity.json(data); // See: https://jersey.java.net/documentation/latest/client.html#d0e4692
	// Se volessi utilizzare il post di tipo "application/x-www-form-urlencoded"
	// Form form = new Form();
	// form.param("user", "XXX");
	// Entity d4 = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED);

	public Response doPost(String restUrl, JsonObject data) {
 
		LOGGER.log(Level.INFO, "ROUTE: " + restUrl);
		String uri = resolveUrl(restUrl);
		LOGGER.log(Level.INFO, "URI: " + restUrl);
		
		LOGGER.log(Level.INFO, "[POST] " + uri);
		LOGGER.log(Level.INFO, "Request body: ");
		JsonUtils.prettyPrint(data);
		
		Client client = HttpClientUtils.newClient();
		WebTarget target = client.target(uri);
		Response response = null;
		try {
			response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					//.header("X-Requested-With", "XMLHttpRequest")
					.post(Entity.json(data));
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
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
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
