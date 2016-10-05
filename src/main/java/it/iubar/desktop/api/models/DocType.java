package it.iubar.desktop.api.models;

public enum DocType {
 
    CEDOLINO(1), F24(2), UNIEMENS(3), DMAG(4), MUT(5), CU(10), M770(11);
    
    
  private final int id;
    DocType(int id) {
        this.id = id;
    }
	public int getId() {
		return this.id;
	}
}
