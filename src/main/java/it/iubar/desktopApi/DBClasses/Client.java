package it.iubar.desktopApi.DBClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.iubar.desktopApi.DBClasses.interfaces.JSONPrinter;

public class Client implements JSONPrinter{

    private int idClient;
    private String mac;
    private int idApp;
    private String version;
    private String platform;
    private String osName;
    private String osVersion;
    private String javaVersion;
    private int titolari;
    private int datori;
    private int lavoratori;
    private String ipWan;
    private String ipLocal;
    private String hostName;
    private String userName;
    private String serverIp;
    private String serverName;
    private String regKey;
    private String actKey;
    private String dbDate;
    private String dbVersion;
    private String infoAdded;
    private String infoUploaded;

    public Client(int idClient, String mac, int idApp, String version, String platform, String osName, String osVersion, String javaVersion, int titolari, int datori, int lavoratori, String ipWan, String ipLocal, String hostName, String userName, String serverIp, String serverName, String regKey, String actKey, String dbDate, String dbVersion, String infoAdded, String infoUploaded) {
        this.setIdClient(idClient);
        this.setMac(mac);
        this.setIdApp(idApp);
        this.setVersion(version);
        this.setPlatform(platform);
        this.setOsName(osName);
        this.setOsVersion(osVersion);
        this.setJavaVersion(javaVersion);
        this.setTitolari(titolari);
        this.setDatori(datori);
        this.setLavoratori(lavoratori);
        this.setIpWan(ipWan);
        this.setIpLocal(ipLocal);
        this.setHostName(hostName);
        this.setUserName(userName);
        this.setServerIp(serverIp);
        this.setServerName(serverName);
        this.setRegKey(regKey);
        this.setActKey(actKey);
        this.setDbDate(dbDate);
        this.setDbVersion(dbVersion);
        this.setInfoAdded(infoAdded);
        this.setInfoUploaded(infoUploaded);
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public void setTitolari(int titolari) {
        this.titolari = titolari;
    }

    public void setDatori(int datori) {
        this.datori = datori;
    }

    public void setLavoratori(int lavoratori) {
        this.lavoratori = lavoratori;
    }

    public void setIpWan(String ipWan) {
        this.ipWan = ipWan;
    }

    public void setIpLocal(String ipLocal) {
        this.ipLocal = ipLocal;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setRegKey(String regKey) {
        this.regKey = regKey;
    }

    public void setActKey(String actKey) {
        this.actKey = actKey;
    }

    public void setDbDate(String dbDate) {
        this.dbDate = dbDate;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public void setInfoAdded(String infoAdded) {
        this.infoAdded = infoAdded;
    }

    public void setInfoUploaded(String infoUploaded) {
        this.infoUploaded = infoUploaded;
    }

    public int getIdClient() {
        return idClient;
    }

    public String getMac() {
        return mac;
    }

    public int getIdApp() {
        return idApp;
    }

    public String getVersion() {
        return version;
    }

    public String getPlatform() {
        return platform;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public int getTitolari() {
        return titolari;
    }

    public int getDatori() {
        return datori;
    }

    public int getLavoratori() {
        return lavoratori;
    }

    public String getIpWan() {
        return ipWan;
    }

    public String getIpLocal() {
        return ipLocal;
    }

    public String getHostName() {
        return hostName;
    }

    public String getUserName() {
        return userName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getServerName() {
        return serverName;
    }

    public String getRegKey() {
        return regKey;
    }

    public String getActKey() {
        return actKey;
    }

    public String getDbDate() {
        return dbDate;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public String getInfoAdded() {
        return infoAdded;
    }

    public String getInfoUploaded() {
        return infoUploaded;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Client client = new Client(idClient, mac, idApp, version, platform, osName, osVersion, javaVersion, titolari, datori, lavoratori, ipWan, ipLocal, hostName, userName, serverIp, serverName, regKey, actKey, dbDate, dbVersion, infoAdded, infoUploaded);
        return client;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            return mapper.writeValueAsString(this.clone());
        } catch (CloneNotSupportedException e) {
            return e.getMessage();
        }
    }
}
