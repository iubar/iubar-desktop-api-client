package it.iubar.desktop.api.models;

public class Client{

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

    public Client(String mac, int idapp, String version, String ip_wan){
        this.setMac(mac);
        this.setIdapp(idapp);
        this.setVersion(version);
        this.setIp_wan(ip_wan);
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setIdapp(int idapp) {
        this.idapp = idapp;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setOs_name(String os_name) {
        this.os_name = os_name;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public void setJava_version(String java_version) {
        this.java_version = java_version;
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

    public void setIp_wan(String ip_wan) {
        this.ip_wan = ip_wan;
    }

    public void setIp_local(String ip_local) {
        this.ip_local = ip_local;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public void setReg_key(String reg_key) {
        this.reg_key = reg_key;
    }

    public void setAct_key(String act_key) {
        this.act_key = act_key;
    }

    public void setDb_date(String db_date) {
        this.db_date = db_date;
    }

    public void setDb_version(String db_version) {
        this.db_version = db_version;
    }

    public void setInfo_added(String info_added) {
        this.info_added = info_added;
    }

    public void setInfo_uploaded(String info_uploaded) {
        this.info_uploaded = info_uploaded;
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
