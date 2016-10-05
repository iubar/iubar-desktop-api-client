package it.iubar.desktop.api.models;

import java.util.Date;

public class ClientModel implements IJsonModel {

    private String mac;
    private int idapp;
    private String version;
    private String os_name;
    private String os_version;
    private String java_version;
    private int titolari;
    private int datori;
    private int lavoratori;
    private String ip_local;
    private String host_name;
    private String user_name;
    private String server_ip;
    private String server_name;
    private String reg_key;
    private String act_key;
    private String db_date;
    private String db_version;

    public ClientModel(String mac, int idapp){
        this.setMac(mac);
        this.setIdapp(idapp);
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

    public String getMac() {
        return mac;
    }

    public int getIdapp() {
        return idapp;
    }

    public String getVersion() {
        return version;
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

	public static ClientModel fatory() {
		ClientModel client = new ClientModel("123325345234134", 2323);
		client.setVersion("8697623");

        client.setOs_name("Windows Vista");
        client.setOs_version("6.3");
        client.setJava_version("");
        client.setTitolari(0);
        client.setDatori(0);
        client.setLavoratori(0);
        client.setIp_local("192.168.32.2");
        client.setHost_name("TWO");
        client.setUser_name("TOMMASO");
        client.setServer_ip("127.0.0.1");
        client.setServer_name("localhost");
        client.setReg_key("");
        client.setAct_key("");
        client.setDb_date("0000-00-00");
        client.setDb_version("00.92");
        return client;
	}

	public void setDb_date(Date date) {
		setDb_date(toString(date));
	}

	private String toString(Date date) {
		final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(sdf);
		return str;
	}

}
