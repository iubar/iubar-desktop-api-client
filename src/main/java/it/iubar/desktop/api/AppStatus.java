package it.iubar.desktop.api;

public enum AppStatus {
	SERIAL_NOT_VALIDATED("Codice attivazione non valido"), 
	SERIAL_VALIDATED("Codice attivazione valido"),
	BLACK_LISTED("Codice attivazione in blacklist"),
	GREY_LISTED("Client in graylist"),
	TIME_HACK("Tentativo di hack del codice attivazione"),
	UNKNWON("Sconosciuto");
	
	 private String desc = null;
	
	 private AppStatus(String desc) {
	    	this.desc = desc;
	    }
	 	@Override
		public String toString() {
	    	return this.desc;	
		}
		public boolean isValidated() {
			if(this == SERIAL_VALIDATED){
				return true;
			}
			return false;
		}		
};
