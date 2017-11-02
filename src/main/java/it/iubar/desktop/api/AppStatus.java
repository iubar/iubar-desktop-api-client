package it.iubar.desktop.api;





public enum AppStatus {
	SERIAL_NOT_VALIDATED("Codice seriale non valido"), 
	SERIAL_VALIDATED("Codice seriale validato"),
	BLACK_LISTED("Codice seriale in lista nera"),
	GREY_LISTED("Client in lista grigia"),
	TIME_HACK("Tentativo di hack del codice seriale"),
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
