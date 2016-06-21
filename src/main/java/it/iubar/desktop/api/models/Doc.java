package it.iubar.desktop.api.models;

public class Doc {
    private int iddoctype;
    private int idtitolare;
    private int mese;
    private int anno;

    public Doc(int iddoctype, int idtitolare, int mese, int anno) {
        this.setIddoctype(iddoctype);
        this.setIdtitolare(idtitolare);
        this.setMese(mese);
        this.setAnno(anno);
    }

    public int getIddoctype() {
        return iddoctype;
    }

    public void setIddoctype(int iddoctype) {
        this.iddoctype = iddoctype;
    }

    public int getIdtitolare() {
        return idtitolare;
    }

    public void setIdtitolare(int idtitolare) {
        this.idtitolare = idtitolare;
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
}
