package it.iubar.desktopApi.DBClasses;

public class Titolare extends JSONPrinter{
    private int idtitolare;
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

    public Titolare(int idtitolare, int idtipo, String cf, String piva, String denom, String cognome, String indirizzo, String email, String tel, int datori, int lavoratori, int idcomune, int idprovincia, String info_added, String info_updated, String chiave_pubblica) {
        this.setIdtitolare(idtitolare);
        this.setIdtipo(idtipo);
        this.setCf(cf);
        this.setPiva(piva);
        this.setDenom(denom);
        this.setCognome(cognome);
        this.setIndirizzo(indirizzo);
        this.setEmail(email);
        this.setTel(tel);
        this.setDatori(datori);
        this.setLavoratori(lavoratori);
        this.setIdcomune(idcomune);
        this.setIdprovincia(idprovincia);
        this.setInfo_added(info_added);
        this.setInfo_updated(info_updated);
        this.setChiave_pubblica(chiave_pubblica);
    }

    public int getIdtitolare() {
        return idtitolare;
    }

    private void setIdtitolare(int idtitolare) {
        this.idtitolare = idtitolare;
    }

    public int getIdtipo() {
        return idtipo;
    }

    private void setIdtipo(int idtipo) {
        this.idtipo = idtipo;
    }

    public String getCf() {
        return cf;
    }

    private void setCf(String cf) {
        this.cf = cf;
    }

    public String getPiva() {
        return piva;
    }

    private void setPiva(String piva) {
        this.piva = piva;
    }

    public String getDenom() {
        return denom;
    }

    private void setDenom(String denom) {
        this.denom = denom;
    }

    public String getCognome() {
        return cognome;
    }

    private void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    private void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    private void setTel(String tel) {
        this.tel = tel;
    }

    public int getDatori() {
        return datori;
    }

    private void setDatori(int datori) {
        this.datori = datori;
    }

    public int getLavoratori() {
        return lavoratori;
    }

    private void setLavoratori(int lavoratori) {
        this.lavoratori = lavoratori;
    }

    public int getIdcomune() {
        return idcomune;
    }

    private void setIdcomune(int idcomune) {
        this.idcomune = idcomune;
    }

    public int getIdprovincia() {
        return idprovincia;
    }

    private void setIdprovincia(int idprovincia) {
        this.idprovincia = idprovincia;
    }

    public String getInfo_added() {
        return info_added;
    }

    private void setInfo_added(String info_added) {
        this.info_added = info_added;
    }

    public String getInfo_updated() {
        return info_updated;
    }

    private void setInfo_updated(String info_updated) {
        this.info_updated = info_updated;
    }

    public String getChiave_pubblica() {
        return chiave_pubblica;
    }

    private void setChiave_pubblica(String chiave_pubblica) {
        this.chiave_pubblica = chiave_pubblica;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Titolare titolare = new Titolare(getIdtitolare(),getIdtipo(),getCf(),getPiva(),getDenom(),getCognome(),getIndirizzo(),getEmail(),getTel(),getDatori(),getLavoratori(),getIdcomune(),getIdprovincia(),getInfo_added(),getInfo_updated(),getChiave_pubblica());
        return titolare;
    }
}
