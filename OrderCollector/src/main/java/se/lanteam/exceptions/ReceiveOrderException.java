package se.lanteam.exceptions;

/**
 * General advertisement exception 
 *
 */
public class ReceiveOrderException extends Exception {
	private static final long serialVersionUID = 5078622028197805638L;
	
	private String orderNumber;
	private String netsetOrderNumber;
	private String conflictingOrderNumber;
	private boolean loggErrorInDb = false;
	
	public ReceiveOrderException(String message, boolean loggInDb) {
		super(message);
		this.loggErrorInDb = loggInDb;
	}
	
	public ReceiveOrderException(String orderNumber, String message, boolean loggInDb) {
		super(message);
		this.orderNumber = orderNumber;
		this.loggErrorInDb = loggInDb;
	}

	public ReceiveOrderException(String orderNumber, String netsetOrderNumber, String conflictingOrderNumber, String message) {
		super(message);
		this.orderNumber = orderNumber;
		this.netsetOrderNumber = netsetOrderNumber;
		this.conflictingOrderNumber = conflictingOrderNumber;
	}

	
	@Override
	public String getMessage() {
		String message = super.getMessage() + ". Ordernummer = " + orderNumber;
		if (netsetOrderNumber != null && netsetOrderNumber.length() > 0) {
			message = message + ". Netset ordernumner = " + netsetOrderNumber;
		}
		if (conflictingOrderNumber != null && conflictingOrderNumber.length() > 0) {
			message = message + ". Redan mottagen order = " + conflictingOrderNumber;
		}
		return message;
	}

	public boolean isLoggErrorInDb() {
		return loggErrorInDb;
	}
}
