package it.iubar.desktopApi.models;

public class Client{

    private int idclient;
    private String mac;
    private int idapp;
    private String version;
    private String platform;
    private String os_name;
    private String os_version;
    private String java_version;
    private int titolari;
    private int datori;
    private int lavoratori;
    private String ip_wan;
    private String ip_local;
    private String host_name;
    private String user_name;
    private String server_ip;
    private String server_name;
    private String reg_key;
    private String act_key;
    private String db_date;
    private String db_version;
    private String info_added;
    private String info_uploaded;

    public Client(int idclient, String mac, int idApp, String version, String platform, String osName, String osVersion, String javaVersion, int titolari, int datori, int lavoratori, String ipWan, String ipLocal, String hostName, String userName, String serverIp, String serverName, String regKey, String actKey, String dbDate, String dbVersion, String infoAdded, String infoUploaded) {
        this.setIdclient(idclient);
        this.setMac(mac);
        this.setIdapp(idApp);
        this.setVersion(version);
        this.setPlatform(platform);
        this.setOs_name(osName);
        this.setOs_version(osVersion);
        this.setJava_version(javaVersion);
        this.setTitolari(titolari);
        this.setDatori(datori);
        this.setLavoratori(lavoratori);
        this.setIp_wan(ipWan);
        this.setIp_local(ipLocal);
        this.setHost_name(hostName);
        this.setUser_name(userName);
        this.setServer_ip(serverIp);
        this.setServer_name(serverName);
        this.setReg_key(regKey);
        this.setAct_key(actKey);
        this.setDb_date(dbDate);
        this.setDb_version(dbVersion);
        this.setInfo_added(infoAdded);
        this.setInfo_uploaded(infoUploaded);
    }

    private void setIdclient(int idclient) {
        this.idclient = idclient;
    }

    private void setMac(String mac) {
        this.mac = mac;
    }

    private void setIdapp(int idapp) {
        this.idapp = idapp;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    private void setPlatform(String platform) {
        this.platform = platform;
    }

    private void setOs_name(String os_name) {
        this.os_name = os_name;
    }

    private void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    private void setJava_version(String java_version) {
        this.java_version = java_version;
    }

    private void setTitolari(int titolari) {
        this.titolari = titolari;
    }

    private void setDatori(int datori) {
        this.datori = datori;
    }

    private void setLavoratori(int lavoratori) {
        this.lavoratori = lavoratori;
    }

    private void setIp_wan(String ip_wan) {
        this.ip_wan = ip_wan;
    }

    private void setIp_local(String ip_local) {
        this.ip_local = ip_local;
    }

    private void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    private void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    private void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    private void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    private void setReg_key(String reg_key) {
        this.reg_key = reg_key;
    }

    private void setAct_key(String act_key) {
        this.act_key = act_key;
    }

    private void setDb_date(String db_date) {
        this.db_date = db_date;
    }

    private void setDb_version(String db_version) {
        this.db_version = db_version;
    }

    private void setInfo_added(String info_added) {
        this.info_added = info_added;
    }

    private void setInfo_uploaded(String info_uploaded) {
        this.info_uploaded = info_uploaded;
    }

    public int getIdclient() {
        return idclient;
    }

    public String getMac() {
        return mac;
    }

    public int getIdapp() {
        return idapp;
    }

    public String getVersion() {
        return version;
    }

    public String getPlatform() {
        return platform;
    }

    public String getOs_name() {
        return os_name;
    }

    public String getOs_version() {
        return os_version;
    }

    public String getJava_version() {
        return java_version;
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

    public String getIp_wan() {
        return ip_wan;
    }

    public String getIp_local() {
        return ip_local;
    }

    public String getHost_name() {
        return host_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public String getServer_name() {
        return server_name;
    }

    public String getReg_key() {
        return reg_key;
    }

    public String getAct_key() {
        return act_key;
    }

    public String getDb_date() {
        return db_date;
    }

    public String getDb_version() {
        return db_version;
    }

    public String getInfo_added() {
        return info_added;
    }

    public String getInfo_uploaded() {
        return info_uploaded;
    }

}
