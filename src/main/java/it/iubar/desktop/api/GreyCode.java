package it.iubar.desktop.api;

public enum GreyCode {
DEFAULT(0, "Not in grey status"),
DOVREBBE_ABBONARSI(1, "Dovrebbe abbonarsi"), // Non più utilizzato, perchè c'è watermark su stampe
NON_HA_PAGATO(2, "Non ha pagato"), // Usato nei confronti di chi non paga la seconda data
BLOCCATO(3, "Bloccato");
 
private int cod;
private String desc;

private GreyCode(int cod, String desc) {
	this.cod = cod;
    this.desc = desc;
    }

 	@Override
	public String toString() {
    	return this.desc;	
	}

	public int getCod() {
		return this.cod;
	}
}
