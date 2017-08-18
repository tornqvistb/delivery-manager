package se.lanteam.model;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.StatusConstants;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class OrderListSearchBean {

	private String query;	
	private String fromDate;
	private String toDate;
	private String status;
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
	
}
