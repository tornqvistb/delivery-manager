package se.lanteam.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import se.lanteam.domain.OrderHeader;

public class DeliveryForm {

	private String query = "";
	private String errorMessage = "";
	private String infoMessage = "";	
	private List<DeliveryStatus> deliveryStati = DeliveryStatus.getFullList();	
	private String deliveryStatus = "";	
	private String receiver = "";
	private List<String> receiverList = new ArrayList<>();
	private String signature = "";	
	private String nameClarification = "";
	private String comment = "";
	private String orderNumber = "";
	private String orderNumbersConcat = "";
	
	protected final static String ORDER_SEPARATOR = "_";
	
	public static final String OTHER_PERSON = "Annan";
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getInfoMessage() {
		return infoMessage;
	}
	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}
	public List<DeliveryStatus> getDeliveryStati() {
		return deliveryStati;
	}
	public void setDeliveryStati(List<DeliveryStatus> deliveryStati) {
		this.deliveryStati = deliveryStati;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public List<String> getReceiverList() {
		return receiverList;
	}
	public void setReceiverList(List<String> receiverList) {
		this.receiverList = receiverList;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getNameClarification() {
		return nameClarification;
	}
	public void setNameClarification(String nameClarification) {
		this.nameClarification = nameClarification;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	@Override
	public String toString() {
		return "DeliveryForm [query=" + query + ", errorMessage=" + errorMessage + ", infoMessage=" + infoMessage
				+ ", deliveryStati=" + deliveryStati + ", deliveryStatus=" + deliveryStatus + ", receiver=" + receiver
				+ ", receiverList=" + receiverList + ", signature=" + signature + ", nameClarification="
				+ nameClarification + ", comment=" + comment + ", orderNumber=" + orderNumber + ", orderNumbersConcat=" + orderNumbersConcat + "]";
	}
	public String getOrderNumbersConcat() {
		return orderNumbersConcat;
	}
	public void setOrderNumbersConcat(String orderNumbersConcat) {
		this.orderNumbersConcat = orderNumbersConcat;
	}
	public void addOrderNumber(String orderNumber) {
		if (!StringUtils.isEmpty(orderNumbersConcat)) {
			orderNumbersConcat = orderNumbersConcat + ORDER_SEPARATOR;
		}
		orderNumbersConcat = orderNumbersConcat + orderNumber;
		String[] orders = orderNumbersConcat.split(ORDER_SEPARATOR);		
	}
	public void removeOrderNumber(String orderNumber) {
		String newValue = "";
		if (!StringUtils.isEmpty(orderNumbersConcat)) {
			String[] orders = orderNumbersConcat.split(ORDER_SEPARATOR);
			boolean first = true;
			for (String order : orders) {
				if (!order.equals(orderNumber)) {
					if (!first) {
						newValue = newValue + ORDER_SEPARATOR;
					}
					newValue = newValue + order;
					first = false;
				}
			}
		}
		orderNumbersConcat = newValue;
	}
	public List<String> getOrdersAsList() {
		List<String> list = new ArrayList<>();
		if (!StringUtils.isEmpty(orderNumbersConcat)) {
			list = Arrays.asList(orderNumbersConcat.split(ORDER_SEPARATOR));
		}
		return list;
	}	
	public void setReceiversFromOrderList(List<OrderHeader> orders) {
		List<String> receivers = new ArrayList<>();
		
		if (orders.size() == 1) {
			receivers = getReceiversFromOrder(orders.get(0));
		} else {
			boolean allEqual = true;
			for (OrderHeader order : orders) {
				if (!order.getFullContactPersonString().equals(orders.get(0).getFullContactPersonString())) {
					allEqual = false;
					break;
				}
			}
			if (allEqual) {
				receivers = getReceiversFromOrder(orders.get(0));
			}
		}
		
		receivers.add(OTHER_PERSON);
		this.receiverList = receivers;
	}
	private List<String> getReceiversFromOrder(OrderHeader order) {
		List<String> receivers = new ArrayList<>();
		if (!StringUtils.isEmpty(order.getContact1Name())) {
			receivers.add(order.getContact1Name());
		}
		if (!StringUtils.isEmpty(order.getContact2Name()) && !order.getContact1Name().equals(order.getContact2Name())) {
			receivers.add(order.getContact2Name());
		}
		
		return receivers;
	}
	public void setStatiFromOrderList() {
		if (this.getOrdersAsList().size() == 1) {
			this.deliveryStati = DeliveryStatus.getFullList();				
		} else {
			this.deliveryStati = DeliveryStatus.getDefaultList();				
		}		
	}	
	
}
