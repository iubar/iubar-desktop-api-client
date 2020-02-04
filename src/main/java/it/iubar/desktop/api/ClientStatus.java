package it.iubar.desktop.api;

public class ClientStatus {

    public static AppStatus app_status = AppStatus.UNKNWON;
	public static AppStatus2 app_status2 = AppStatus2.UNKNWON;
	
	public static String getString() {
		String s1 = ClientStatus.app_status.toString();

		String status = s1;
		if(ClientStatus.app_status2 != AppStatus2.UNKNWON) {
			String s2 = ClientStatus.app_status2.toString();
			s1 = s1 + " / " + s2;
		}
		if(ClientStatus.app_status == AppStatus.UNKNWON) {
			status = "<sconosciuto>";
		}
		return status;
	}

	public static boolean isAppRegistered() {
		if(ClientStatus.app_status2 == AppStatus2.OK) {
			return true;
		}
		return false;
	}
	
}
