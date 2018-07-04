package it.iubar.desktop.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DocModel extends RootModel implements IJsonModel, Cloneable {
    
	private DocType doctype;
	private String cf;
	private int mese;
    private int anno;
    
    public DocModel(DocType iddoctype, int mese, String cf, int anno) {
        this.setDoctype(iddoctype);
        this.setCf(cf);
        this.setMese(mese);
        this.setAnno(anno);
    }

    public DocModel() {
		// TODO Auto-generated constructor stub
	}

	public int getIddoctype() {
		int iddoctype= 0;
		if(this.doctype!=null){
			iddoctype = this.doctype.getId();
		}
        return iddoctype;
    }

	@JsonIgnore
	public DocType getDoctype() {
        return this.doctype;
    }

    public void setDoctype(DocType doctype) {
        this.doctype = doctype;
    }

    public String getCf() {
		return this.cf;
	}
    public void setCf(String cf)
    {
    	this.cf = cf;
    }

    public int getMese() {
        return mese;
    }

    public void setMese(int mese) {
        this.mese = mese;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }
	
	 @Override
	public DocModel clone() throws CloneNotSupportedException {
	        return (DocModel) super.clone();
	    }


	 
}
