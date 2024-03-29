package it.iubar.desktop.api.models;

import java.util.List;
import java.util.Set;

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
    public Set<Integer> contratti_dettaglio;
    public Set<String> contratti_cnel;
    
    public TitolareModel() {
    	super();
    }

    public String getPiva() {
        return this.piva;
    }

    public void setPiva(String piva) {
    	if(piva !=null && piva.equals("")) {
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.piva = null;
    	}else {
    		this.piva = piva;
    	}
    }

    public String getDenom() {
        return this.denom;
    }

    public void setDenom(String denom) {
    	if(denom !=null && denom.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.denom = null;
    	}else {
    		this.denom = denom;
    	}
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
    	if(cognome !=null && cognome.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.cognome = null;
    	}else {
        this.cognome = cognome;
    	}
    }

    public String getIndirizzo() {
        return this.indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
    	if(indirizzo !=null && indirizzo.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.indirizzo = null;
    	}else {
        this.indirizzo = indirizzo;
    	}
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
    	if(email!=null && email.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.email = null;
    	}else {
        this.email = email;
    	}
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
    	if(tel !=null && tel.equals("")){
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.tel = null;
    	}else {
        this.tel = tel;
    	}
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
	
	public void setContrattiDettaglio(Set<Integer> contratti_dettaglio) {
		this.contratti_dettaglio = contratti_dettaglio;
	}
	
	public void setContrattiCnel(Set<String> contratti_cnel) {
		this.contratti_cnel = contratti_cnel;
	}
 
    public static void main(String[] args) {
		TitolareModel model = new TitolareModel();
		model.cf="AAAA";
		model.cognome="Borgo";
		String json = model.asJson();
		System.out.println(json);		
	}
	
}
