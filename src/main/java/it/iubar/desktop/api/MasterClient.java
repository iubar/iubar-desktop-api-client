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
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterClient {

    private final static Logger LOGGER = Logger.getLogger(MasterClient.class.getName());

    private final String IS_AUTH_VALUE = "is_auth";
    private final String HOST_VALUE = "host";
    private final String PORT_VALUE = "port";
    private final String PATH_VALUE = "path";
    private final String IS_UNIQUE_VALUE = "is_unique";
    private final String USER_VALUE = "user";
    private final String API_KEY_VALUE = "api_key";


    private final String INSERT_CLIENT = "/client";
    private final String INSERT_TITOLARE = "/titolare";
    private final String INSERT_DATORE = "/datore";
    private final String INSERT_CCNL = "/ccnl";

    private String user;
    private String apiKey;
    private String configDir;
    private boolean isAuth;
    private String url;
    private boolean uniqueUrl;

    public MasterClient(String configDir) throws IOException {
        this.setConfigDir(configDir);
        this.setUpIni();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getConfigDir() {
        return configDir;
    }

    public void setConfigDir(String configDir) {
        this.configDir = configDir;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUniqueUrl() {
        return uniqueUrl;
    }

    public void setUniqueUrl(boolean uniqueUrl) {
        this.uniqueUrl = uniqueUrl;
    }

    //Sets all the variables looking at the ".ini" file.
    private void setUpIni() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(this.getConfigDir());
        properties.load(inputStream);

        String user, apiKey, host;

        String auth = properties.getProperty(IS_AUTH_VALUE, "false");

        try {
            if(fromStringToBool(auth)){
                apiKey = properties.getProperty(API_KEY_VALUE);
                user = properties.getProperty(USER_VALUE);
                this.setUser(user);
                this.setApiKey(apiKey);
            }
            host = properties.getProperty(HOST_VALUE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, this.getConfigDir() + " is not well-formatted");
            throw new RuntimeException("config.ini file is not well-formatted");
        }

        String unique = properties.getProperty(IS_UNIQUE_VALUE, "false");
        String port = properties.getProperty(PORT_VALUE, "80");
        String path = properties.getProperty(PATH_VALUE, "/");

        LOGGER.log(Level.FINE, this.getConfigDir() + " parsed succesfully");
        
        this.setAuth(fromStringToBool(auth));
        this.setUniqueUrl(fromStringToBool(unique));

        host = normalizeHost(host);
        path = normalizePath(path);
        port = normalizePort(port);

        this.setUrl("http://" + host + ":" + port  + path);
    }

    private boolean fromStringToBool(String s){
        if(s.equalsIgnoreCase("true"))
            return true;
        else
            return false;
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

        if(obj == null){
            LOGGER.log(Level.SEVERE, "Object to send cannot be nullable.");
            throw new RuntimeException("Object to send cannot be nullable.");
        }

        //Initialization jersey client
        Client client = Client.create();
        //set destination url
        WebResource webResource = null;

        if (!uniqueUrl) {
            if(obj instanceof it.iubar.desktop.api.models.Client)
                url += INSERT_CLIENT;
            else if (obj instanceof Datore)
                url += INSERT_DATORE;
            else if(obj instanceof Titolare)
                url += INSERT_TITOLARE;
            else if(obj instanceof Ccnl)
                url += INSERT_CCNL;
        }

        webResource = client.resource(this.getUrl());

        JSONObject jsonObject = null;

        try {
            if(this.isAuth()){
                jsonObject = new JSONObject();
                jsonObject.put(USER_VALUE, this.getUser());
                jsonObject.put("timestamp", getTimeStamp());
                JSONObject tojson = new JSONObject(JSONPrinter.toJson(obj));
                jsonObject.put("data", tojson);
                jsonObject.put("signature", encryptedData(tojson.toString()));
                System.out.println(jsonObject.toString());
            }else{
                jsonObject = new JSONObject(JSONPrinter.toJson(obj));
            }
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Jackson could not convert correctly", JsonProcessingException.class);
            throw new RuntimeException("Jackson ha problemi a convertire la classe.");
        }

        //execution query
        ClientResponse response = webResource.accept("application/json").type(MediaType.APPLICATION_JSON_TYPE).header("X-Requested-With", "XMLHttpRequest").post(ClientResponse.class, jsonObject.toString());

        JSONObject jsonResponse = new JSONObject(response.getEntity(String.class));

        //if code is not 200, throw exception
        int stat = response.getStatus();
        if (stat != 200) {
            LOGGER.log(Level.SEVERE, "Error found in response: CODE " + stat);
            throw new ClientException(response.getStatus(), jsonResponse.get("response").toString());
        }
    }

    private String encryptedData(String data){
        String payload = this.getUrl() + this.getUser() + this.getTimeStamp() + this.getApiKey() + data;
        String algo = "HmacSHA256";
        String keyString = this.getApiKey();
        try{
            Mac sha256_HMAC = Mac.getInstance(algo);
            SecretKeySpec secret_key = new SecretKeySpec(keyString.getBytes(), algo);
            sha256_HMAC.init(secret_key);
            return Base64.encodeBase64String(sha256_HMAC.doFinal(payload.getBytes()));
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Unable to generate encypted data.");
            throw new RuntimeException("Unable to generate encypted data.");
        }
    }

    private String getTimeStamp(){
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date());
    }

}
