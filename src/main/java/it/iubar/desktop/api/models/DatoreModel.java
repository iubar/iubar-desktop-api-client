package it.iubar.desktop.api.models;

public class DatoreModel implements IJsonModel {
    
	private int idapp = 0;
	private String cf;
    private String piva;
    private String denom;
    private int sub;
    private int para;
    private String email;
    private String tel;
    private String idcomune;
 

    public DatoreModel() {
    	super();
    }

    public void setIdapp(int idapp) {
        this.idapp = idapp;
    }
    
    public int getIdapp() {
        return this.idapp;
    }
    
    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getDenom() {
        return denom;
    }

    public void setDenom(String denom) {
        this.denom = denom;
    }

    public int getSub() {
        return sub;
    }

    public void setSub(int sub) {
        this.sub = sub;
    }

    public int getPara() {
        return para;
    }

    public void setPara(int para) {
        this.para = para;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdcomune() {
        return idcomune;
    }

    public void setIdcomune(String idcomune) {
        this.idcomune = idcomune;
    }



}
