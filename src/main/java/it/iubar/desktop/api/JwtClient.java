package it.iubar.desktop.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
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
import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;

 public class JwtClient extends AuthHttpClient implements IHttpClient {

	private final static Logger LOGGER = Logger.getLogger(JwtClient.class.getName());

 
	public JwtClient() {
		super();
	}
 	
	/**
	 * @param secret
	 * @return
	 */
	public String createToken(String secret) {
		String token = null;
		try {

			Calendar issuedAt = new GregorianCalendar();
			Calendar notBefore = (Calendar) issuedAt.clone();
			notBefore.add(Calendar.SECOND, -600); // 10 minutes
			Calendar expiresAt = (Calendar) issuedAt.clone();
			expiresAt.add(Calendar.SECOND, 1200); // 20 minutes
		 
			String jwtId = "1";

		    Algorithm algorithm = Algorithm.HMAC512(secret); //  using the HS512 algorithm
		    Builder builder = JWT.create()
			        .withIssuer("auth0")
			        .withIssuedAt(issuedAt.getTime())
			        .withNotBefore(notBefore.getTime())
			        .withJWTId(jwtId)
			        .withExpiresAt(expiresAt.getTime());
		    		    
		    token = builder.sign(algorithm);
		} catch (JWTCreationException e){
		    //Invalid Signing configuration / Couldn't convert Claims.
			e.printStackTrace();
		}
		return token;
	}
	
	public void demo() {
//		String token = createToken();
//		System.out.println("JWT (auth0): " + token);
	}
	
//	public void demo2() {
//		// From https://firebase.google.com/docs/auth/admin/create-custom-tokens
//		// To create custom tokens with the Firebase Admin SDK, you must have a service account. Follow the Admin SDK setup instructions for more information on how to initialize the Admin SDK with a service account.
//		// https://firebase.google.com/docs/admin/setup/
//		
//		try {
//			FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
//			FirebaseOptions options = new FirebaseOptions.Builder()
//					  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//					  .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
//					  .build();
//					FirebaseApp.initializeApp(options);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		
//		String uid = "some-uid";	
//		try {
//			String customToken = FirebaseAuth.getInstance().createCustomTokenAsync(uid).get();
//			System.out.println("JWT (firebase): " + customToken);
// 
//			Map<String, Object> additionalClaims = new HashMap<String, Object>();
//			additionalClaims.put("premiumAccount", true);
//
//			String customToken2 = FirebaseAuth.getInstance()
//			    .createCustomTokenAsync(uid, additionalClaims).get();
//			System.out.println("JWT CUSTOM (firebase): " + customToken2);
// 
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
 	protected JSONObject genAuth2(String destUrl) {
		String secret = this.getApiKey();
		//String tsEncoded = rawUrlEncode(ts); // inutile perchè ci pensa la libreria client 
		JSONObject authData = new JSONObject();
		authData.put("email", this.getUser());
		authData.put("token", this.createToken(secret));
		return authData;
	}
	
	@Override
	public Response get(String restUrl) {
		restUrl = resolveUrl(restUrl);
		System.out.println("GET:" + restUrl);
		Client client = HttpClient.newClient();
		WebTarget target = client.target(restUrl); // il metodo codifica il parametro (la stringa che rappresenta l'url) in modo analogo alla funzione PHP rawurlencode()
		if (this.isAuth()) {
			JSONObject dataToSend = genAuth(restUrl);
			target = target.queryParam("email", dataToSend.get("email")).queryParam("token", dataToSend.get("token"));
		}
		Response response = target.request(MediaType.APPLICATION_JSON).accept("application/json").header("X-Requested-With", "XMLHttpRequest").get();
		return response;
	}
 
}
