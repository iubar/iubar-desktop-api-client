package it.iubar.desktop.api.models;

public class DocModel implements IJsonModel, Cloneable {
    private int iddoctype;
    private String cf;
	private int mese;
    private int anno;
	private int qnt;

    public DocModel(int iddoctype, String cf, int mese, int anno) {
        this.setIddoctype(iddoctype);
        this.setMese(mese);
        this.setAnno(anno);
    }

    public DocModel() {
		// TODO Auto-generated constructor stub
	}

	public int getIddoctype() {
        return iddoctype;
    }

    public void setIddoctype(int iddoctype) {
        this.iddoctype = iddoctype;
    }

    public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
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

	public void setDocType(DocType docType) {
		setIddoctype(docType.getId());		
	}

	public void setQnt(int qnt) {
		this.qnt = qnt;
	}
	public int getQnt() {
		return this.qnt;
	}	
	
	 @Override
	public DocModel clone() throws CloneNotSupportedException {
	        return (DocModel) super.clone();
	    }


	 
}
