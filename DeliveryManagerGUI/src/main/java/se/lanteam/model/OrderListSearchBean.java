package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import se.lanteam.constants.StatusConstants;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class OrderListSearchBean {

	private String query;	
	private String fromDate;
	private String toDate;
	private String status;
	private Long customerGroupId;
	private String customerNumber;
	private List<String> stati = new ArrayList<String>();
	private Pageable maxRows;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public OrderListSearchBean(String query, String fromDate, String toDate, String status) {
		super();
		this.query = query;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.status = status;
	}
	public OrderListSearchBean() {		
		super();
		this.query = "";
		this.fromDate = "";
		this.toDate = "";
		this.status = StatusConstants.ORDER_STATUS_GROUP_ACTIVE;
	}
	public Long getCustomerGroupId() {
		return customerGroupId;
	}
	public void setCustomerGroupId(Long customerGroupId) {
		this.customerGroupId = customerGroupId;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public List<String> getStati() {
		return stati;
	}
	public void setStati(List<String> stati) {
		this.stati = stati;
	}
	public Pageable getMaxRows() {
		return maxRows;
	}
	public void setMaxRows(Pageable maxRows) {
		this.maxRows = maxRows;
	}
	public String getQueryWithWildcards() {
		if (StringUtils.isEmpty(query)) {
			return "%";
		} else {
			return "%" + query + "%";
		}
	}
	
}
