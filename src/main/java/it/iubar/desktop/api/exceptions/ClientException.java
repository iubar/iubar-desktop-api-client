package it.iubar.desktop.api.exceptions;

public class ClientException extends Exception {

    private int code;

    public ClientException (int code, String message){
        super(message);
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
