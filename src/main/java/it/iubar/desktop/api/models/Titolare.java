package it.iubar.desktop.api.models;

public class Titolare{
    private int idtipo;
    private String cf;
    private String piva;
    private String denom;
    private String cognome;
    private String indirizzo;
    private String email;
    private String tel;
    private int datori;
    private int lavoratori;
    private int idcomune;
    private int idprovincia;
    private String info_added;
    private String info_updated;
    private String chiave_pubblica;

    public Titolare(int idcomune, int idprovincia) {
        this.setIdcomune(idcomune);
        this.setIdprovincia(idprovincia);
    }

    public int getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(int idtipo) {
        this.idtipo = idtipo;
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
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

    public int getDatori() {
        return datori;
    }

    public void setDatori(int datori) {
        this.datori = datori;
    }

    public int getLavoratori() {
        return lavoratori;
    }

    public void setLavoratori(int lavoratori) {
        this.lavoratori = lavoratori;
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

    public String getInfo_added() {
        return info_added;
    }

    public void setInfo_added(String info_added) {
        this.info_added = info_added;
    }

    public String getInfo_updated() {
        return info_updated;
    }

    public void setInfo_updated(String info_updated) {
        this.info_updated = info_updated;
    }

    public String getChiave_pubblica() {
        return chiave_pubblica;
    }

    public void setChiave_pubblica(String chiave_pubblica) {
        this.chiave_pubblica = chiave_pubblica;
    }

}
