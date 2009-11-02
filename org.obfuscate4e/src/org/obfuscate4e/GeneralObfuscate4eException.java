package org.obfuscate4e;

public class GeneralObfuscate4eException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3410713335150482171L;

	public static final String NL = System.getProperty("line.separator");
	public static final String ERROR_NO_CONFIGURATORS = "Sorry, there are currently no obfuscators registered.";
	public static final String ERROR_NO_PREFERENCE = "Sorry, there is no preferred obfuscator specified."
		+ NL
		+ "Go to the obfuscation preference page and select the preferred obfuscator."
		+ NL + "Window > Preferences > Obfuscation";

	
	public GeneralObfuscate4eException(String message){
		super(message);
	}

}
