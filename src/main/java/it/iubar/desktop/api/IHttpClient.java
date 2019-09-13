package it.iubar.desktop.api;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;

public interface IHttpClient {

	<T> JSONObject send(ModelsList<? super T> modelList)  throws Exception;

	<T> JSONObject send(IJsonModel client) throws Exception;

	Response get(String string);

	JSONObject send(String url1, IJsonModel model) throws Exception;

	JSONObject responseManager(Response response) throws Exception;
 
	
}
 
