package it.iubar.desktop.api.models;

import java.util.logging.Logger;

 

public class MacUtils {
	
	private static final Logger LOGGER = Logger.getLogger(MacUtils.class.getName());
	 public static final String MAC_ADDRESS_SEPARATOR_1 = ":";
	public static final String MAC_ADDRESS_SEPARATOR_2 = "-";
	
	private static boolean isMacWithoutSep(String mac) {
		boolean b = false;
		if(isNotEmpty(mac)) {
			String rexEx = "^[0-9A-F]{2}[0-9A-F]{2}[0-9A-F]{2}[0-9A-F]{2}[0-9A-F]{2}[0-9A-F]{2}$";
			java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(rexEx);
					java.util.regex.Matcher matcher = pattern.matcher(mac);						
		        if(matcher.matches()){
		            return true;
		        }
			}
		return b;
	}

	public static String formatMac(String mac) {		
		if (!MacUtils.isMacValid(mac)){
			int len = mac.length();
			if (len != 6 && len !=11){				
				LOGGER.warning("Lunghezza indirizzo mac non valida: " + mac + " (" + len + ")");
				mac = "<not valid>";
			}
			mac =  mac.replaceAll(MacUtils.MAC_ADDRESS_SEPARATOR_1, "").toLowerCase();
			if (MacUtils.isMacWithoutSep(mac)){
				mac = mac.toUpperCase().replaceAll("(.{2})", "$1" + MacUtils.MAC_ADDRESS_SEPARATOR_2).substring(0,17);
			}
		}
		return mac;
	}
					/**
					 * @see https://en.wikipedia.org/wiki/MAC_address
					 * 
					 */
					public static boolean isMacValid(String mac) {
						boolean b = false;
						if(isNotEmpty(mac)) {
						String rexEx = "^[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}$";
						java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(rexEx);
								java.util.regex.Matcher matcher = pattern.matcher(mac);						
					        if(matcher.matches()){
					            return true;
					        }
						}
						if(!b) {
							LOGGER.warning("Mac non valido: " + mac);
						}
					        return b;
	 
					}

					private static boolean isNotEmpty(String str) {
						if(str!=null && str.length()>0) {
							return true;
						}
						return false;
					}
					
}
