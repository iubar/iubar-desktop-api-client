package it.iubar.desktop.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.Ccnl;
import it.iubar.desktop.api.models.Datore;
import it.iubar.desktop.api.models.Titolare;
import it.iubar.desktop.api.services.JSONPrinter;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterClient {

    private final static Logger LOGGER = Logger.getLogger(MasterClient.class.getName());

    private final String IS_AUTH_VALUE = "is_auth";
    private final String HOST_VALUE = "host";
    private final String PORT_VALUE = "port";
    private final String PATH_VALUE = "path";

    private String configDir;
    private String authValue;
    private boolean isAuth;

    private String url;

    public MasterClient(String configDir) throws IOException {
        this.setAuthValue(IS_AUTH_VALUE);
        this.setConfigDir(configDir);
        this.url = getUrl();
    }

    public MasterClient(String configDir, String isAuthValue) throws IOException {
        this.setAuthValue(isAuthValue);
        this.setConfigDir(configDir);
        this.url = getUrl();
    }

    public void setAuthValue(String authValue) {
        this.authValue = authValue;
    }

    public void setConfigDir(String path) throws IOException{
        this.configDir = path;
        this.isAuth = isAuth();
    }

    public String getConfigDir() {
        return configDir;
    }

    public String getAuthValue() {
        return authValue;
    }

    private boolean isAuth() throws IOException {

        boolean isAuth;

        Properties prop = new Properties();
        InputStream input = new FileInputStream(this.getConfigDir());
        prop.load(input);
        String result = prop.getProperty(this.getAuthValue(), "false");
        if(result.equalsIgnoreCase("true"))
            isAuth = true;
        else
            isAuth = false;
        LOGGER.log(Level.FINE, "Output given: " + result);
        if (input != null) {
            input.close();
            LOGGER.log(Level.FINE, "File closed.");
        }

        return isAuth;
    }

    private String getUrl() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(this.getConfigDir());
        properties.load(inputStream);

        String host = properties.getProperty(HOST_VALUE);
        if(host == null){
            LOGGER.log(Level.SEVERE, "Could not find 'host' in ini file.");
            throw new RuntimeException("Could not find 'host' in ini file.");
        }else{
            host = normalizeHost(host);
        }

        String port = properties.getProperty(PORT_VALUE, "80");
        port = normalizePort(port);

        String path = properties.getProperty(PATH_VALUE, "");
        path = normalizePath(path);

        return "http://" + host + ":" + port  + path ;
    }

    private String normalizePath(String s) {
        if(!s.equalsIgnoreCase(""))
            if(!s.substring(0,1).equalsIgnoreCase("/"))
                s = "/" + s;

        return s;
    }

    private String normalizeHost(String s){
        if(s.contains("http://"))
            s = s.replace("http://", "");
        if(s.contains("https://"))
            s = s.replace("https://", "");
        if(s.contains(":"))
            s = s.substring(0,s.indexOf(":"));

        return s;
    }

    private String normalizePort(String s){
        if(!s.equalsIgnoreCase("80"))
            if(s.contains(":"))
                s = s.replace(":", "");
        return s;
    }

    public <T> void send(T obj) throws ClientException {

        //Initialization jersey client
        Client client = Client.create();
        //set destination url
        WebResource webResource = null;

        //FIXME : remove manual data
        if(obj instanceof it.iubar.desktop.api.models.Client)
            url += "/insertClient";
        else if (obj instanceof Datore)
            url += "/insertDatore";
        else if(obj instanceof Titolare)
            url += "/insertTitolare";
        else if(obj instanceof Ccnl)
            url += "/insertCcnl";

        webResource = client.resource(url);

        String toJson = null;

        try {
            toJson = JSONPrinter.toJson(obj);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Jackson could not convert correctly", JsonProcessingException.class);
            throw new RuntimeException("Jackson ha problemi a convertire la classe.");
        }

        //execution query
        ClientResponse response = webResource.accept("application/json").type(MediaType.APPLICATION_JSON_TYPE).header("X-Requested-With", "XMLHttpRequest").post(ClientResponse.class, toJson);

        JSONObject jsonResponse = new JSONObject(response.getEntity(String.class));
        System.out.println(jsonResponse.toString());

        //if code is not 200, throw exception
        int stat = response.getStatus();
        if (stat != 200) {
            LOGGER.log(Level.SEVERE, "Error found in response: CODE " + stat);
            throw new ClientException(response.getStatus(), jsonResponse.get("response").toString());
        }
    }

}
