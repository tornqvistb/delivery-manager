package se.lanteam.constants;

public class LimStringUtil {
	
	public static String firstOrderNo = "000000";
	public static String lastOrderNo = "999999";
	
	public static String NVL(String inStr, String replString) {
		String result = inStr;
		if (inStr == null || inStr.length() == 0) {
			result = replString;
		}
		return result;
	}

}
