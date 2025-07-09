package it.iubar.desktop.api;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
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
	private static final int DEF_CONNECT_TIMEOUT = 10000;
	private static final int DEF_READ_TIMEOUT = 10000;
	protected String url = null;

	public static Client newClient() {
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, DEF_CONNECT_TIMEOUT);
		client.property(ClientProperties.READ_TIMEOUT, DEF_READ_TIMEOUT);
		return client;
	}

	public static Client newClient2025() {
		Client client = null;

		// Crea un SSLContext con la versione desiderata (es. TLSv1.3 o TLSv1.2)
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLSv1.3");
			sslContext.init(null, null, null); // Usa truststore/keystore di default
			

            // Imposta i protocolli TLS supportati (forza TLSv1.3)
            SSLParameters sslParams = sslContext.getDefaultSSLParameters();
            sslParams.setProtocols(new String[]{"TLSv1.3"});
            
		} catch (NoSuchAlgorithmException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		} catch (KeyManagementException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}

		if (sslContext != null) {
			// Costruisci il client JAX-RS con il SSLContext custom
			client = ClientBuilder.newBuilder()
					// .property("jersey.config.client.followRedirects", true)
					.sslContext(sslContext)
					//.hostnameVerifier((hostname, session) -> true) // opzionale: ignora verifica host
					.connectTimeout(5, TimeUnit.SECONDS) // Timeout di connessione
					.readTimeout(10, TimeUnit.SECONDS) // Timeout di lettura
					.build();
 

		}
		return client;
	}

	/**
	 * Crea il client con autenticazione user/password, e ignora la validità del
	 * certificato SSL
	 * 
	 */
	public static Client newClientProtected(String user, String password) {
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

		Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
		client.property(ClientProperties.CONNECT_TIMEOUT, 2000)
				.register(HttpAuthenticationFeature.basic(user, password));

		return client;
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
