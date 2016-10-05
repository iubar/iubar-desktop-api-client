package it.iubar.desktop.api.models;

public class CcnlModel implements IJsonModel {
    
	private String idccnl;

    public CcnlModel(String idccnl){
        this.setIdccnl(idccnl);
    }

    public String getIdccnl() {
        return idccnl;
    }

    public void setIdccnl(String idccnl) {
        this.idccnl = idccnl;
    }

}
