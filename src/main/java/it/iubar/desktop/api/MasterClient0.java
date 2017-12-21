package it.iubar.desktop.api;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

abstract public class MasterClient0 {

	private final static Logger LOGGER = Logger.getLogger(MasterClient0.class.getName());

	private String user = null;
	private String apiKey = null;
	private boolean isAuth = false;
	private String url = null;

	public MasterClient0() {
		super();
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

	protected boolean isAuth() {
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
	 * 
	 * Il metodo implementa il seguente codice PHP: $hash =
	 * base64_encode(hash_hmac('sha256', 'Message', 'secret', true));
	 * 
	 * @param destUrl
	 * @param data
	 * @return
	 */
	public static String encryptData(String message, String secret) {
		String hash_encoded = null;
		final String algo = "HmacSHA256";
		if (secret == null || secret.equals("")) {
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

	/**
	* RFC_3339 timestamp
	* @see https://tools.ietf.org/html/rfc3339
	* Coordinated Universal Time (UTC)
   	* Because the daylight saving rules for local time zones are so convoluted and can change based on local law at 
   	* unpredictable times, true interoperability is best achieved by using Coordinated Universal Time (UTC).
	* This specification does not cater to local time zone rules.
	*/
	private String getTimeStamp() {
		String ts = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date()); // RFC_3339 timestamp																							// format
		return ts;
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
		restUrl = resolveUrl(restUrl);
		if (this.isAuth()) {
			JSONObject data2 = genAuth(restUrl, data);
			Entity<String> d3 = Entity.json(data2.toString());
			return post(restUrl, d3);
		} else {
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
		Entity<String> d2 = Entity.entity(data.toString(), MediaType.APPLICATION_JSON);
		Entity<String> d3 = Entity.json(data.toString()); // See: https://jersey.java.net/documentation/latest/client.html#d0e4692
		return post(restUrl, d3);
	}

	public Response post(String restUrl, Entity<String> d3) {
		System.out.println("restUrl: " + restUrl);
		// System.out.println("post: " + d3.toString());
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(restUrl);

		Response response = target.request(MediaType.APPLICATION_JSON).accept("application/json")
				.header("X-Requested-With", "XMLHttpRequest").post(d3);
		return response;
	}

	/*
	 * Il seguente metodo implementa la logica di risoluzione delle rotte
	 * dettata dall'RFC3986, ovvero la stessa utilizzata da Guzzle e descritta
	 * nel manuale dello stesso progetto
	 * 
	 * @see: http://docs.guzzlephp.org/en/5.3/clients.html
	 */
	private String resolveUrl(String restUrl) {
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
		WebTarget target = client.target(restUrl); // il metodo codifica il parametro (la stringa che rappresenta l'url) in modo analogo alla funzione PHP rawurlencode()
		if (this.isAuth()) {
			JSONObject dataToSend = genAuth(restUrl);
			String ts = String.valueOf(dataToSend.get("ts"));
			String hash = String.valueOf(dataToSend.get("hash"));
			target = target.queryParam("user", dataToSend.get("user")).queryParam("ts", dataToSend.get("ts")).queryParam("hash", dataToSend.get("hash"));
		}
		Response response = target.request(MediaType.APPLICATION_JSON).accept("application/json").header("X-Requested-With", "XMLHttpRequest").get();
		return response;
	}

	private JSONObject genAuth(String destUrl) {
		JSONObject authData = genAuth2(destUrl);
		return authData;
	}

	private JSONObject genAuth2(String destUrl) {
		String ts = this.getTimeStamp();
		String hash_argument = destUrl + this.getUser() + ts + this.getApiKey();
		String secret = this.getApiKey();
		String hash = encryptData(hash_argument, secret);
		//String tsEncoded = rawUrlEncode(ts); // inutile perchè ci pensa la libreria client 
		JSONObject authData = new JSONObject();
		authData.put("user", this.getUser());
		authData.put("ts", ts);
		authData.put("hash", hash);

		return authData;
	}

}
