package it.iubar.desktop.api.models;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 

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

	/**
	 * 
	 * uguale a: text.replaceAll("(.{" + period + "})", "$1" + insert)
	 * 
	 * @param text
	 * @param insert
	 * @param period
	 * @return
	 */
	public static String insert(String text, String insert, int period) {
	    Pattern p = Pattern.compile("(.{" + period + "})", Pattern.DOTALL);
	    Matcher m = p.matcher(text);
	    return m.replaceAll("$1" + insert);
	}
	
	public static String formatMac(String mac) {
		// Pulisco la stringa
		mac =  mac.replaceAll(MacUtils.MAC_ADDRESS_SEPARATOR_1, "").toUpperCase();
		// Verifico se si tratta di un indirizzo mac senza separatori
		if (!MacUtils.isMacValid(mac) && MacUtils.isMacWithoutSep(mac)){
			mac = insert(mac, MAC_ADDRESS_SEPARATOR_2, 2).substring(0,17);
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
