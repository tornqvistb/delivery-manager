package se.lanteam.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.OrderHeader;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SearchBean {

	private List<OrderHeader> orderList;
	private Long customerGroupId;
	private Date fromDate;
	private Date toDate;
	private String fromOrderNo;
	private String toOrderNo;
	private List<CustomerCustomField> customerCustomFields = new ArrayList<CustomerCustomField>();

	public List<OrderHeader> getOrderList() {
		return orderList;
	}

	public void populate(List<OrderHeader> orderList, Long customerGroupId, Date fromDate, Date toDate, String fromOrderNo,
			String toOrderNo, List<CustomerCustomField> customerCustomFields) {
		this.orderList = orderList;
		this.customerGroupId = customerGroupId;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.fromOrderNo = fromOrderNo;
		this.toOrderNo = toOrderNo;
		this.customerCustomFields = customerCustomFields;
	}

	public void setOrderList(List<OrderHeader> orderList) {
		this.orderList = orderList;
	}

	public Long getCustomerGroupId() {
		return customerGroupId;
	}

	public void setCustomerGroupId(Long customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getFromOrderNo() {
		return fromOrderNo;
	}

	public void setFromOrderNo(String fromOrderNo) {
		this.fromOrderNo = fromOrderNo;
	}

	public String getToOrderNo() {
		return toOrderNo;
	}

	public void setToOrderNo(String toOrderNo) {
		this.toOrderNo = toOrderNo;
	}

	public List<CustomerCustomField> getCustomerCustomFields() {
		return customerCustomFields;
	}

	public void setCustomerCustomFields(List<CustomerCustomField> customerCustomFields) {
		this.customerCustomFields = customerCustomFields;
	}
	
	
}
