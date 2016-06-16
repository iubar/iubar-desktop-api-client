package it.iubar.desktop.api;

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
        return "http://" + properties.getProperty(HOST_VALUE) + ":" + properties.getProperty(PORT_VALUE, "80") + properties.getProperty(PATH_VALUE, "/");
    }



}
