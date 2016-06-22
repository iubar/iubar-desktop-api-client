package it.iubar.desktop.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import it.iubar.desktop.api.exceptions.ClientException;
import it.iubar.desktop.api.models.*;
import it.iubar.desktop.api.services.JSONPrinter;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final String INSERT_DOC = "/doc";
    private final String INSERT_MAC = "/list/mac";

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

    private String getUser() {
        return user;
    }

    private void setUser(String user) {
        this.user = user;
    }

    private String getApiKey() {
        return apiKey;
    }

    private void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private String getConfigDir() {
        return configDir;
    }

    private void setConfigDir(String configDir) {
        this.configDir = configDir;
    }

    private boolean isAuth() {
        return isAuth;
    }

    private void setAuth(boolean auth) {
        isAuth = auth;
    }

    private String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    private boolean isUniqueUrl() {
        return uniqueUrl;
    }

    private void setUniqueUrl(boolean uniqueUrl) {
        this.uniqueUrl = uniqueUrl;
    }

    //Sets all the variables looking at the ".ini" file.
    private void setUpIni() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(this.getConfigDir());
        properties.load(inputStream);

        String user;
        String apiKey;
        String host;

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
        return s.equalsIgnoreCase("true");
    }

    private String normalizePath(String path) {
        String finalPath = path;

        if(!finalPath.equalsIgnoreCase("") && !finalPath.substring(0,1).equalsIgnoreCase("/"))
            finalPath = "/" + finalPath;

        return finalPath;
    }

    private String normalizeHost(String host){
        String finalHost = host;

        if(finalHost.contains("http://"))
            finalHost = finalHost.replace("http://", "");
        if(finalHost.contains("https://"))
            finalHost = finalHost.replace("https://", "");
        if(finalHost.contains(":"))
            finalHost = finalHost.substring(0,finalHost.indexOf(":"));

        return finalHost;
    }

    private String normalizePort(String port){
        String finalPort = port;

        if(!finalPort.equalsIgnoreCase("80") && (finalPort.contains(":")))
            finalPort = finalPort.replace(":", "");
        return finalPort;
    }

    public <T> void send(T obj) throws ClientException {

        cantBeNull(obj);

        //Initialization jersey client
        Client client = Client.create();
        //set destination url
        WebResource webResource;

        JSONObject jsonObject = new JSONObject();

        if (!this.isUniqueUrl())
            webResource = client.resource(masterRouter(obj));
        else {//TODO: remove hardcoded data
            jsonObject.put("whatis", obj.getClass().getName());
            webResource = client.resource(this.getUrl());
        }

        try {
            if(this.isAuth()){ //TODO: remove hardcoded data
                jsonObject.put(USER_VALUE, this.getUser());
                jsonObject.put("timestamp", getTimeStamp());
                JSONObject tojson = new JSONObject(JSONPrinter.toJson(obj));
                jsonObject.put("data", tojson);
                jsonObject.put("signature", encryptedData(tojson.toString()));
            }else{
                jsonObject = new JSONObject(JSONPrinter.toJson(obj));
            }
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Jackson could not convert the object correctly.", JsonProcessingException.class);
            throw new RuntimeException("Jackson could not convert the object correctly.");
        }

        //execution query
        ClientResponse response = webResource.accept("application/json").type(MediaType.APPLICATION_JSON_TYPE).header("X-Requested-With", "XMLHttpRequest").post(ClientResponse.class, jsonObject.toString());

        responseManager(response);
    }

    public ListMac checkMac(String macAddress){

        cantBeNull(macAddress);

        Client client = Client.create();

        WebResource webResource = client.resource(this.getUrl() + INSERT_MAC + "/" + macAddress);

        ClientResponse response = webResource.header("X-Requested-With", "XMLHttpRequest").accept("application/json").get(ClientResponse.class);

        JSONObject jsonObject = responseManager(response);

        if(jsonObject.getBoolean("black-list")){
            return new ListMac(true);
        }else{
            JSONObject greyList = jsonObject.getJSONObject("grey-list");
            return new ListMac(greyList.getInt("idreason"), greyList.getString("desc"));
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

    private <T> boolean isNull(T obj){
        return obj == null;
    }

    private <T> void cantBeNull(T obj){
        if(isNull(obj)){
            LOGGER.log(Level.SEVERE, "Object to send cannot be nullable.");
            throw new RuntimeException("Object to send cannot be nullable.");
        }
    }

    private <T> String masterRouter(T obj){
        String urlToSend = this.getUrl();

        if(obj instanceof it.iubar.desktop.api.models.Client)
            urlToSend += INSERT_CLIENT;
        else if (obj instanceof Datore)
            urlToSend += INSERT_DATORE;
        else if(obj instanceof Titolare)
            urlToSend += INSERT_TITOLARE;
        else if(obj instanceof Ccnl)
            urlToSend += INSERT_CCNL;
        else if(obj instanceof Doc)
            urlToSend += INSERT_DOC;

        return urlToSend;
    }

    private JSONObject responseManager(ClientResponse response){

        int status = response.getStatus();

        if(status == 201 || status == 200){
            JSONObject answer = new JSONObject(response.getEntity(String.class));
            try {
                LOGGER.log(Level.FINE, "Query ok, code: " + status + ", rows affected: " + answer.getString("response"));
            } catch (JSONException e) {
                LOGGER.log(Level.FINE, "Query ok, code: " + status);
            }
            return answer;
        } else if (status == 400){
            LOGGER.log(Level.SEVERE, "Bad request, code: " + status);
            throw new RuntimeException("Bad request, code: " + status);
        } else if( status == 404){
            LOGGER.log(Level.SEVERE, "Not found, code: " + status);
            throw new RuntimeException("Not found, code: " + status);
        } else if (status == 500){
            LOGGER.log(Level.SEVERE, "Internal server error, code: " + status);
            throw new RuntimeException("Internal server error, code: " + status);
        } else {
            LOGGER.log(Level.SEVERE, "Unknown error, code: " + status);
            throw new RuntimeException("Unknown error, code: " + status);
        }

    }

}
