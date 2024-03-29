package it.iubar.desktop.api.models;

public class ClientModel extends RootModel implements IJsonModel {

    private String mac;
    private String ip;
    private int idapp;
    private int titolari;
    private int datori;
    private int lavoratori;
    private int cedolini_ultimi_12_mesi;
    private String cf;

    public ClientModel(){
        super(); 
     }
    
    public ClientModel(String mac, int idapp){
        this.setMac(mac);
        this.setIdapp(idapp);
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIdapp(int idapp) {
        this.idapp = idapp;
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
    
    public void setCedolini_ultimi_12_mesi(int cedolini_ultimi_12_mesi) {
        this.cedolini_ultimi_12_mesi = cedolini_ultimi_12_mesi;
    }

    public String getMac() {
        return this.mac;
    }
    
    public String getIp() {
        return this.ip;
    }

    public int getIdapp() {
        return this.idapp;
    }

    public int getTitolari() {
        return this.titolari;
    }

    public int getDatori() {
        return this.datori;
    }

    public int getLavoratori() {
        return this.lavoratori;
    }
    
    public int getCedolini_ultimi_12_mesi() {
        return this.cedolini_ultimi_12_mesi;
    }

	public String getCf() {
		return this.cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

}
