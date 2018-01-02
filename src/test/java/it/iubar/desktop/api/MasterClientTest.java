package it.iubar.desktop.api;

 
import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class MasterClientTest extends MasterClientAbstract {

	@Override
	protected MasterClient clientFactory() {
    	MasterClient masterClient = null;
    	 
			masterClient = new MasterClient();
			// masterClient.loadConfig();
			// oppure
			// masterClient.setBaseUrl("http://httpbin.org/post");
			// oppure
			masterClient.setBaseUrl(MasterClient.ROUTE_BASE);
 
    	return masterClient;
	}
 
}