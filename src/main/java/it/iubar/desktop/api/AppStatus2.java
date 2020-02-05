package it.iubar.desktop.api;

public enum AppStatus2 {
	OK("Ok"),
	SUPERATO_LIMITE_UTILIZZO("Superato limite utilizzo"),
	UNKNWON("Sconosciuto");
	
	 private String desc = null;
	
	 private AppStatus2(String desc) {
	    	this.desc = desc;
	    }
	 	@Override
		public String toString() {
	    	return this.desc;	
		}		
};
