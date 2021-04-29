package se.lanteam.model;

import java.util.List;

public class RestOrder {

	public static final String ROW_TYPE_HEADER = "H";
	public static final String ROW_TYPE_LINE = "L";
	public static final String FIELD_SEPARATOR = ";";
	
	String orderNumber;
	String originalOrderNumber;
	List<RestOrderLine> orderLines;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOriginalOrderNumber() {
		return originalOrderNumber;
	}
	public void setOriginalOrderNumber(String restOrderNumber) {
		this.originalOrderNumber = restOrderNumber;
	}
	public List<RestOrderLine> getOrderLines() {
		return orderLines;
	}
	public void setOrderLines(List<RestOrderLine> orderLines) {
		this.orderLines = orderLines;
	}
	
}
