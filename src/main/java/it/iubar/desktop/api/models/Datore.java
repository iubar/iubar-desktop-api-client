package it.iubar.desktop.api.models;

public class Datore {
    private int iddatore;
    private String cf;
    private String piva;
    private String denom;
    private String sub;
    private String para;
    private String email;
    private String tel;
    private int idcomune;
    private int idprovincia;

    public Datore(int iddatore, String cf, String piva, String denom, String sub, String para, String email, String tel, int idcomune, int idprovincia) {
        this.setIddatore(iddatore);
        this.setCf(cf);
        this.setPiva(piva);
        this.setDenom(denom);
        this.setSub(sub);
        this.setPara(para);
        this.setEmail(email);
        this.setTel(tel);
        this.setIdcomune(idcomune);
        this.setIdprovincia(idprovincia);
    }

    public int getIddatore() {
        return iddatore;
    }

    public void setIddatore(int iddatore) {
        this.iddatore = iddatore;
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

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
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

    public int getIdcomune() {
        return idcomune;
    }

    public void setIdcomune(int idcomune) {
        this.idcomune = idcomune;
    }

    public int getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(int idprovincia) {
        this.idprovincia = idprovincia;
    }

}
