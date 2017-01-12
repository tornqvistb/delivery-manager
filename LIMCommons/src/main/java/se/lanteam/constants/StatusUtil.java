package se.lanteam.constants;

import org.thymeleaf.util.ArrayUtils;

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

	public static boolean isActiveStatus(String status) {
		boolean result = false;
		if (StatusConstants.ORDER_STATUS_GROUP_ACTIVE.equals(status) || ArrayUtils.contains(StatusConstants.ACTIVE_STATI, status)) {
			result = true;
		}
		return result;
	}
}
