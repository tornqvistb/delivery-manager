package se.lanteam.exceptions;

/**
 * General advertisement exception 
 *
 */
public class ReceiveOrderException extends Exception {
	private static final long serialVersionUID = 5078622028197805638L;
	
	private String orderNumber;
	
	public ReceiveOrderException(String orderNumber, String message) {
		super(message);
		this.orderNumber = orderNumber;
	}
		
	@Override
	public String getMessage() {
		return super.getMessage() + ". Order number = " + orderNumber;
	}
}
