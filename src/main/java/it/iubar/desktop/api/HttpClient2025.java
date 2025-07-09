package it.iubar.desktop.api;

import java.util.logging.Logger;

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

 
public class HttpClient2025 extends AuthHttpClient implements IHttpClient {

	private final static Logger LOGGER = Logger.getLogger(HttpClient2025.class.getName());

	public HttpClient2025() {
		super();
	}
	
	@Override
 	protected JsonObject genAuth2(String destUrl) {
 
		return null;
	}
 
	@Override
	public Response get(String restUrl) {
		restUrl = resolveUrl(restUrl);
		System.out.println("GET:" + restUrl);
		Client client = HttpClientUtils.newClient();
		WebTarget target = client.target(restUrl); // il metodo codifica il parametro (la stringa che rappresenta l'url) in modo analogo alla funzione PHP rawurlencode()
 
		Response response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				//.header("X-Requested-With", "XMLHttpRequest")
				.get();
		return response;
	}
 
}