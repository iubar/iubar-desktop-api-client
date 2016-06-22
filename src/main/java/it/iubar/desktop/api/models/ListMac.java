package it.iubar.desktop.api.models;

public class ListMac {

    private boolean blackList;
    private int codeGreyList;
    private String descGreyList;

    public ListMac(boolean blackList) {
        this.setBlackList(blackList);
        this.setCodeGreyList(0);
        this.setDescGreyList("");
    }

    public ListMac(int codeGreyList, String descGreyList) {
        this.setBlackList(false);
        this.setCodeGreyList(codeGreyList);
        this.setDescGreyList(descGreyList);
    }

    public boolean isBlackList() {
        return blackList;
    }

    private void setBlackList(boolean blackList) {
        this.blackList = blackList;
    }

    public boolean isGreyList(){
        return codeGreyList != 0;
    }

    public int getCodeGreyList() {
        return codeGreyList;
    }

    private void setCodeGreyList(int codeGreyList) {
        this.codeGreyList = codeGreyList;
    }

    public String getDescGreyList() {
        return descGreyList;
    }

    private void setDescGreyList(String descGreyList) {
        this.descGreyList = descGreyList;
    }
}
