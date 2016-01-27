package se.lanteam.constants;

public class StatusUtil {

	public static String getOrderStatusDisplay(String status) {
		String result = status;
		for (String[] statusPair : StatusConstants.ORDER_STATUS_MATRIX) {
			if (statusPair[0].equals(status)) {
				result = statusPair[1];
				break;
			}
		}
		return result;
	}
	
}
