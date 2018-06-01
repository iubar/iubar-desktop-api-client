package it.iubar.desktop.api.models;

public class DatoreModel extends RootModel implements IJsonModel {
    
	private int idapp = 0;

    private String piva;
    private String denom;
    private int sub;
    private int para;
    private String email;
    private String tel;
    private String idcomune;

	private String cf;
 
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
        return this.cf;
    }
    public void setCf(String cf)
    {
    	this.cf = cf;
    }
    

    public String getPiva() {
        return this.piva;
    }

    public void setPiva(String piva) {
    	if(piva !=null && piva.equals(""))
    	{
    		throw new IllegalArgumentException();
    	}
        this.piva = piva;
    }

    public String getDenom() {
        return denom;
    }

    public void setDenom(String denom) {
    	if(denom !=null && denom.equals(""))
    	{
    		throw new IllegalArgumentException();
    	}
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
    	if(email !=null && email.equals(""))
    	{
    		throw new IllegalArgumentException();
    	}
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
    	if(tel !=null && tel.equals(""))
    	{
    		throw new IllegalArgumentException();
    	}
        this.tel = tel;
    }

    public String getIdcomune() {
        return idcomune;
    }

    public void setIdcomune(String idcomune) {
    	if(idcomune !=null && idcomune.equals(""))
    	{
    		throw new IllegalArgumentException();
    	}
        this.idcomune = idcomune;
    }

}
