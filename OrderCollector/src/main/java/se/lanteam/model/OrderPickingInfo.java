package se.lanteam.model;

import java.util.List;

public class OrderPickingInfo {

	public static final int STATUS_PARTLY_PICKED = 1;
	public static final int STATUS_FULLY_PICKED = 2;
	public static final String ROW_TYPE_HEADER = "H";
	public static final String ROW_TYPE_LINE = "L";
	public static final String FIELD_SEPARATOR = ";";
	
	String orderNumber;
	int status;
	String restOrderNumber;
	List<PickedOrderLine> pickedLines;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getRestOrderNumber() {
		return restOrderNumber;
	}
	public void setRestOrderNumber(String restOrderNumber) {
		this.restOrderNumber = restOrderNumber;
	}
	public List<PickedOrderLine> getPickedLines() {
		return pickedLines;
	}
	public void setPickedLines(List<PickedOrderLine> pickedLines) {
		this.pickedLines = pickedLines;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "OrderPickingInfo [orderNumber=" + orderNumber + ", status=" + status + ", restOrderNumber="
				+ restOrderNumber + ", pickedLines=" + pickedLines + "]";
	}
	
}
