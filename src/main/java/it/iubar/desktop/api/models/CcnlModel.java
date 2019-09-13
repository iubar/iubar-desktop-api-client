package it.iubar.desktop.api.models;

public class CcnlModel extends RootModel implements IJsonModel {
    
	private String idccnl;
//	private int idapp = 0;

    public CcnlModel(){
       super(); 
    }

    public String getIdccnl() {
        return this.idccnl;
    }

//    public void setIdapp(int idapp) {
//        this.idapp  = idapp;
//    }
//    
//    public int getIdapp() {
//        return this.idapp;
//    }    
    
    public void setIdccnl(String idccnl) {
        this.idccnl = idccnl;
    }

}
