package it.iubar.desktop.api;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
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
					.connectTimeout(5, TimeUnit.SECONDS) // Timeout di connessione
					.readTimeout(10, TimeUnit.SECONDS) // Timeout di lettura
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

}
