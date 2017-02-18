package it.iubar.desktop.api.models;

public enum DocType {
 
    CEDOLINO(1), // (invio dati quando il foglio paga aquisice la numerazione)
    F24(2), 	 // (invio dati solo se su disco non vi è file con stesso nome)
    UNIEMENS(3), // (invio dati solo se su disco non vi è file con stesso nome)
    DMAG(4), 
    MUT(5), 
    CU(10), 
    M770(11);
    
    
  private final int id;
    DocType(int id) {
        this.id = id;
    }
	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.id);
	}
}
