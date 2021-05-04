package it.iubar.desktop.api;

import jakarta.ws.rs.core.Response;

import it.iubar.desktop.api.models.IJsonModel;
import it.iubar.desktop.api.models.ModelsList;
import jakarta.json.JsonObject;

public interface IHttpClient {

	<T> JsonObject send(ModelsList<? super T> modelList)  throws Exception;

	<T> JsonObject send(IJsonModel client) throws Exception;

	Response get(String string);

	JsonObject  send(String url1, IJsonModel model) throws Exception;

	JsonObject  responseManager(Response response) throws Exception;
 
	
}
 
