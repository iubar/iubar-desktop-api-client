package it.iubar.desktop.api.models;

public class HrModel extends RootModel implements IJsonModel {

	public String piva;
	public String denom;
	public int lavoratori;    
	public String email;
	public String tel;
	public String nome;
	public String email2;
	public String mac;
	public boolean user;
 
    
    public HrModel() {
    	super();
    }
 

    public void setPiva(String piva) {
    	if(piva !=null && piva.equals("")) {
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.piva = null;
    	}else {
        this.piva = piva;
    	}
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

 

    public void setNome(String nome) {
    	if(nome !=null && nome.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.nome = null;
    	}else {
        this.nome = nome;
    	}
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
 
    public void setEmail2(String email) {
    	if(this.email2!=null && this.email2.equals(""))
    	{
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.email2 = null;
    	}else {
        this.email2 = email;
    	}
    }
    
    public void setTel(String tel) {
    	if(tel !=null && tel.equals("")){
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.tel = null;
    	}else {
        this.tel = tel;
    	}
    }
 
    public void setMac(String mac) {
    	if(mac !=null && mac.equals("")){
    		// Evito che la Rest Api inserisca la stringa vuota nel DB
    		this.mac = null;
    	}else {
        this.mac = mac;
    	}
    }
	
    public void setLavoratori(int n) {
        this.lavoratori = n;
    }
 
    public static void main(String[] args) {
		HrModel model = new HrModel();
		
		model.setPiva("AAAA");
		model.setDenom("AAAA");
		model.setLavoratori(10);    
		model.setEmail("AAAA");
		model.setTel(null);
		model.setNome("AAAA");
		model.setEmail2("AAAA");
		model.setMac("AAAA");
		model.setUser(true);
		String json = model.asJson();
		System.out.println(json);		
	}

	public void setUser(boolean b) {
		this.user = b;
	}
	
}
