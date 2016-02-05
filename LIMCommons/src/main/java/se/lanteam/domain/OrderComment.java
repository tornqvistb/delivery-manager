package se.lanteam.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import se.lanteam.constants.StatusConstants;

@Entity
public class OrderComment {

	private Long id;
	private String orderLine;
	private String message;
	private Date creationDate;
	private OrderHeader orderHeader;
	private String status;

	
	
	public OrderComment() {
		super();
		this.status = StatusConstants.ORDER_STATUS_NEW;
		this.creationDate = new Date();
	}
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@ManyToOne()
	public OrderHeader getOrderHeader() {
		return orderHeader;
	}
	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}
	public String getOrderLine() {
		return orderLine;
	}
	public void setOrderLine(String orderLine) {
		this.orderLine = orderLine;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Transient
	public String getOrderLineDisplay() {
		String result = String.valueOf(orderLine);
		if ("0".equals(result)) {
			result = "Generellt";
		}
		return result;		
	}
	@Transient
	public String getCreationDateDisplay() {		
		String result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(creationDate);
		return result;		
	}
	@Transient
	public boolean relatesToOrderLine() {
		boolean result = true;
		if ("0".equals(orderLine)) {
			result = false;
		}
		return result;		
	}
	
}
