package it.iubar.desktop.api;

import org.apache.commons.codec.binary.Base64;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

 
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @deprecated uso un client con autenticazione tramite scambio di hash
 * ma l'endpoint common.iubar.it non utlizza questo tipo di autenticazione
 */
@Deprecated
public class HmacClient extends AuthHttpClient implements IHttpClient {

	private final static Logger LOGGER = Logger.getLogger(HmacClient.class.getName());

	public HmacClient() {
		super();
	}
	
	@Override
 	protected JsonObject genAuth2(String destUrl) {
		String ts = this.getTimeStamp();
		String hash_argument = destUrl + this.getUser() + ts + this.getApiKey();
		String secret = this.getApiKey();
		String hash = encryptData(hash_argument, secret);
		//String tsEncoded = rawUrlEncode(ts); // inutile perchè ci pensa la libreria client 
		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
				.add("user", this.getUser())
				.add("ts", ts)
				.add("hash", hash);
 
		JsonObject  authData = objectBuilder.build();
		return authData;
	}

 	public static String encryptData(String message, String secret) {
	String hash_encoded = null;
	final String algo = "HmacSHA256";
	if (secret == null || secret.equals("")) {
		LOGGER.log(Level.SEVERE, "apikey is null or empty");
	}
	try {
		Mac sha256_HMAC = Mac.getInstance(algo);
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), algo);
		sha256_HMAC.init(secret_key);
		byte[] hash = sha256_HMAC.doFinal(message.getBytes());
		hash_encoded = Base64.encodeBase64String(hash);
	} catch (Exception e) {
		LOGGER.log(Level.SEVERE, "Unable to generate encypted data.");
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
	
	@Override
	public Response get(String restUrl) {
		restUrl = resolveUrl(restUrl);
		System.out.println("GET:" + restUrl);
		Client client = HttpClientUtils.newClient();
		WebTarget target = client.target(restUrl); // il metodo codifica il parametro (la stringa che rappresenta l'url) in modo analogo alla funzione PHP rawurlencode()
		if (this.isAuth()) {
			JsonObject dataToSend = genAuth(restUrl);
//			String ts = String.valueOf(dataToSend.get("ts"));
//			String hash = String.valueOf(dataToSend.get("hash"));
			target = target.queryParam("user", dataToSend.getString("user")).queryParam("ts", dataToSend.getString("ts")).queryParam("hash", dataToSend.getString("hash"));
		}
		Response response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				//.header("X-Requested-With", "XMLHttpRequest")
				.get();
		return response;
	}
	
	
}