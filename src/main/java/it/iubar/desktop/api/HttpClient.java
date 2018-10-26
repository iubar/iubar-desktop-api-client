package it.iubar.desktop.api;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

	private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());

	public static Client newClient() {
		ClientConfig configuration = new ClientConfig();
		configuration.property(ClientProperties.CONNECT_TIMEOUT, 1500);
		configuration.property(ClientProperties.READ_TIMEOUT, 3000);
		Client client = ClientBuilder.newClient(configuration);
		return client;
	}
	protected String url = null;

	public static JSONObject getAnswer(Response response) {
		JSONObject answer = null;
		String output = response.readEntity(String.class);
 
		try {
			answer = new JSONObject(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}
	public static boolean toBoolean(String str) {
		boolean b = false;
		if (str != null) {
			if (str.equals("1")) {
				b = true;
			} else if (str.toLowerCase().equals("true")) {
				b = true;
			} else if (str.toLowerCase().equals("yes")) {
				b = true;
			}
		}
		return b;
	}

}
