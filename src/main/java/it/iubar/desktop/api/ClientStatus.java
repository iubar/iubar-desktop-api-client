package it.iubar.desktop.api;

public class ClientStatus {

    public static AppStatus app_status = AppStatus.UNKNWON;
	public static AppStatus2 app_status2 = AppStatus2.UNKNWON;
	
	public static void updateGreyListStatus2(int return_code) {		
		ClientStatus.app_status2 = AppStatus2.UNKNWON;
		if (return_code==GreyCode.DOVREBBE_ABBONARSI.getCod()){
			ClientStatus.app_status = AppStatus.GREY_LISTED;
			ClientStatus.app_status2 = AppStatus2.SUPERATO_LIMITE_UTILIZZO;
		}else if(return_code==GreyCode.NON_HA_PAGATO.getCod()){
			ClientStatus.app_status = AppStatus.GREY_LISTED;
			ClientStatus.app_status2 = AppStatus2.FORCE_PAYMENT;
		}else if (return_code==GreyCode.BLOCCATO.getCod()){
			ClientStatus.app_status = AppStatus.GREY_LISTED;
			ClientStatus.app_status2 = AppStatus2.DUMMY_ERROR;
		}else{
			ClientStatus.app_status2 = AppStatus2.OK;
		}
	}

	public static String getString() {
		String s1 = ClientStatus.app_status.toString();
		String s2 = ClientStatus.app_status2.toString();
		String status = s1 + " / " + s2;
		return status;
	}
	
}
