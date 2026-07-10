package it.iubar.desktop.api;

public class RestApiConsts {

	public static final String CRM_BASE_ROUTE = "https://common.iubar.it/crm/v1";
 
	public static final String IUBAR_HR_BASE_URL = "https://hr.iubar.it";
	
	public static final String PROD_URL = IUBAR_HR_BASE_URL;
	public static final String DEV_URL = "http://192.168.0.131:3000";
	public static final String STAGE_URL = "http://192.168.0.107:3013";
	public static final String LOCAL_URL = "http://localhost:3000";
	
	@Deprecated
	public static final String IUBAR_HR_BASE_API_URL = PROD_URL + "/api/v1";
	@Deprecated
	public static final String IUBAR_HR_OAUTH_LOGIN_URL = PROD_URL + "/oauth/token";
	
	
	  
}
