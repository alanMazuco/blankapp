package br.eti.amazu.infra.util;

import java.text.Normalizer;

public class RegexUtil {
	
	private static final String LIMIT_CHAR = "[a-zA-Z 0-9áàâãäéèêëíìîïóòôõöúùûüçñÁÀÂÃÄÉÈÊËÍÌÎÏÓÒÔÕÖÚÙÛÜÇÑ]+";
	private static final String LIMIT_CHAR_NOT_BLANK_SPACE = "[a-zA-Z0-9_áàâãäéèêëíìîïóòôõöúùûüçñÁÀÂÃÄÉÈÊËÍÌÎÏÓÒÔÕÖÚÙÛÜÇÑ]+";
	
	private RegexUtil() {
		//do not compliance
	}
			
	public static String removeAcentos(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

	public static String removeAcentosEspacos(String str) {
		str = str.replace(" ", "_");
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}
	
	public static String getLimitChar() {
		return LIMIT_CHAR;
	}
	
	public static String getLimitCharNotBlankSpace() {
		return LIMIT_CHAR_NOT_BLANK_SPACE;
	}
	
}