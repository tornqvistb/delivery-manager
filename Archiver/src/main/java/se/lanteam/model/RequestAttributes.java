package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.SystemProperty;

public class RequestAttributes {

	private String orderStatus;
	private String thanksMessage;
	private String query;
	private String errorMessage;
	private String infoMessage;
	private String fromDate;
	private String toDate;
	
	public RequestAttributes() {
		super();
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getThanksMessage() {
		return thanksMessage;
	}

	public void setThanksMessage(String thanksMessage) {
		this.thanksMessage = thanksMessage;
	}

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

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
