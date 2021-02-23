package it.iubar.desktop.api;

public class ClientStatus {

    public static AppStatus app_status = AppStatus.UNKNWON;
	
	public static String getString() {
		String status = ClientStatus.app_status.toString();
		if (ClientStatus.app_status == AppStatus.UNKNWON) {
			status = "<sconosciuto>";
		}
		
		return status;
	}

}
