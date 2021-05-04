package it.iubar.desktop.api.models;

public class TitolareModel extends RootModel implements IJsonModel {
    
    private String cf;
    private String piva;
    private String denom;
    private String cognome;
    private String indirizzo;
    private String email;
    private String tel;
    private int datori;
    private int lavoratori;

    
    public TitolareModel() {
    	super();
    }

    public String getPiva() {
        return this.piva;
    }

    public void setPiva(String piva) {
    	if(piva !=null && piva.equals("")) {
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		piva = null;
    	}
        this.piva = piva;
    }

    public String getDenom() {
        return this.denom;
    }

    public void setDenom(String denom) {
    	if(denom !=null && denom.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		denom = null;
    	}
        this.denom = denom;
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
    	if(cognome !=null && cognome.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		cognome = null;
    	}
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
    	if(indirizzo !=null && indirizzo.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		indirizzo = null;
    	}
        this.indirizzo = indirizzo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
    	if(email!=null && email.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		email = null;
    	}
        this.email = email;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
    	if(tel !=null && tel.equals("")){
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		tel = null;
    	}
        this.tel = tel;
    }

    public int getDatori() {
        return this.datori;
    }

    public void setDatori(int datori) {
        this.datori = datori;
    }

    public int getLavoratori() {
        return this.lavoratori;
    }

    public void setLavoratori(int lavoratori) {
        this.lavoratori = lavoratori;
    }

	public String getCf() {
		return this.cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}
 
    public static void main(String[] args) {
		TitolareModel model = new TitolareModel();
		model.cf="AAAA";
		model.cognome="Borgo";
		String json = model.asJson();
		System.out.println(json);		
	}
	
}
