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

public class MasterClient {

	private final static Logger LOGGER = Logger.getLogger(MasterClient.class.getName());

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
       
    
	private String user = null;
	private String apiKey = null;
	private boolean isAuth = false;
	private String url = null;

	public void loadConfigFromFile(String cfgFile) throws IOException {
		File file = new File(cfgFile);
		InputStream is = new FileInputStream(file);
		this.setUpIni(is);
	}

	public MasterClient() throws IOException {
		super();
	}
	
	public void loadConfigFromJar(){
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

	public void setUser(String user) {
		this.user = user;
	}

	private String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
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
		if (!finalPath.equalsIgnoreCase("")
				&& !finalPath.substring(0, 1).equalsIgnoreCase("/")){
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
		if(idApp!=null){
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

	private JSONObject genAuth(String destUrl) {
		JSONObject authData = genAuth2(destUrl);
		return authData;
	}
	
	private JSONObject genAuth(String destUrl, JSONObject jsonObj) {
		JSONObject authData = genAuth2(destUrl);
		if(jsonObj!=null){
			authData.put("data", jsonObj);
		}
		return authData;
	}
	
	private JSONObject genAuth(String destUrl, JSONArray jsonArray) {
		JSONObject authData = genAuth2(destUrl);
		if(jsonArray!=null){
			authData.put("data", jsonArray);
		}
		return authData;
	}
	
	private JSONObject genAuth2(String destUrl) {
		String ts =  this.getTimeStamp();
		String hash_argument = destUrl + this.getUser() + ts + this.getApiKey();		
		String secret = this.getApiKey();
		String hash = encryptData(hash_argument, secret);
		
		String tsEncode = rawUrlEncode(ts);
		
		JSONObject authData = new JSONObject();
		authData.put(USER_VALUE, this.getUser());
		authData.put("ts", ts);
		authData.put("hash", hash);
		return authData;
	}

	/**
	 * Il metodo implementa la funzione PHP rawurlencode()
	 * @see: http://php.net/manual/en/function.rawurlencode.php
	 * @param string
	 * @return string
	 */
	private String rawUrlEncode(String string) {
		String encoded = string.replaceAll("\\+", "%2B"); // analogo a rawurlencode di Php
		encoded = encoded.replaceAll(":", "%3A"); // analogo a rawurlencode di Php
		return encoded; 
	}

	public Response post(String restUrl, final JSONArray data) {
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			JSONObject data2 = genAuth(restUrl, data);
			Entity<String> d3 = Entity.json(data2.toString());
			return post(restUrl, d3);
		}else{			
			Entity<String> d3 = Entity.json(data.toString());
			return post(restUrl, d3);
		}
	}

	public Response post(String restUrl, JSONObject data) {
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			data = genAuth(restUrl, data);
		}		
		Entity<String> d1 = Entity.text(data.toString());
		Entity<String> d2 = Entity.entity(data.toString() , MediaType.APPLICATION_JSON);
		Entity<String> d3 = Entity.json(data.toString()); // See: https://jersey.java.net/documentation/latest/client.html#d0e4692
		return post(restUrl, d3);
	}
	
	public Response post(String restUrl, Entity<String> d3) {
		System.out.println("restUrl: " + restUrl);
		// System.out.println("post: " + d3.toString());
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(restUrl);

		Response response = target
				.request(MediaType.APPLICATION_JSON)
				.accept("application/json")
				.header("X-Requested-With", "XMLHttpRequest")
				.post(d3);
		return response;				
	}
 

	/*
	 * Il seguente metodo implementa la logica di risoluzione delle rotte dettata dall'RFC3986,
	 * ovvero la stessa utilizzata da Guzzle e descritta nel manuale dello stesso progetto 
	 * @see: http://docs.guzzlephp.org/en/5.3/clients.html
	 */
	private String resolveUrl(String restUrl) {
		String baseUrl = this.getBaseUrl();
		if(!restUrl.startsWith("http")){		
			if(!isAbsoluteRoute(restUrl)){
				String lastChar = baseUrl.substring(baseUrl.length() - 1);
				if(lastChar.equals("/")){
					restUrl = baseUrl + restUrl;
				}else{
					restUrl = getRootUrl(baseUrl) + "/" + restUrl;
				}
			}else{
				restUrl = getRootUrl(baseUrl) + restUrl;
			}
		}else{
			// la rotta è nel formato "http://..."
		}
		return restUrl;
	}

	private boolean isAbsoluteRoute(String restUrl) {
		String s = restUrl.substring(0, 1);
		if(s.equals("/")){
			return true;
		}
		return false;
	}

	private String getRootUrl(String strUrl) {
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String host = url.getHost();
		String protocol = url.getProtocol();
		int port = url.getPort();
		String baseUrl = protocol + "://" + host + ":" + port;
		return baseUrl;
	}

	public Response get(String restUrl) {
		restUrl = resolveUrl(restUrl);
		System.out.println(restUrl);
		Client client = ClientBuilder.newClient();	
		WebTarget target = client.target(restUrl);	
		if (this.isAuth()) {
			 JSONObject dataToSend = genAuth(restUrl);
			 target = target.queryParam(USER_VALUE, dataToSend.get(USER_VALUE))
					 .queryParam("ts", dataToSend.get("ts"))
					 .queryParam("hash", dataToSend.get("hash"));
		}		

		Response response = target
				.request(MediaType.APPLICATION_JSON)
				.accept("application/json")
				.header("X-Requested-With", "XMLHttpRequest")
				.get();				
		return response;
	}

	/**
	 * 
	 * Il metodo implementa il seguente codice PHP:
	 * $hash = base64_encode(hash_hmac('sha256', 'Message', 'secret', true));
	 * 
	 * @param destUrl
	 * @param data
	 * @return
	 */
	public static String encryptData(String message, String secret) {
		String hash_encoded = null;
		final String algo = "HmacSHA256";
		if(secret==null || secret.equals("")){
			throw new RuntimeException("apikey is null or empty");
		}
		try {
			Mac sha256_HMAC = Mac.getInstance(algo);
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), algo);
			sha256_HMAC.init(secret_key);
			byte[] hash = sha256_HMAC.doFinal(message.getBytes());
			hash_encoded = Base64.encodeBase64String(hash);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unable to generate encypted data.");
			throw new RuntimeException("Unable to generate encypted data.");
		}
		return hash_encoded;
	}

	private String getTimeStamp() {
		String ts = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date()); // RFC_3339 format
		return ts;
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
		if(!lastChar.equals("/")){			
			urlToSend = urlToSend + "/";
		}
		if (obj instanceof it.iubar.desktop.api.models.ClientModel) {
			urlToSend += INSERT_CLIENT;
		}else if (obj instanceof it.iubar.desktop.api.models.DocModel) {
				urlToSend += INSERT_DOCUMENTI;			
		} else if (obj instanceof ModelsList) {
			ModelsList ml = ((ModelsList) obj);
			if(ml.getSize()>0){
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
			}else{
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
			if(answer.has("response")){
				resp = answer.getString("response");
			}
			
			try {
				String msg =  "Query ok, code: " + status+ ", rows affected: " + resp;
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
