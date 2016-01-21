package se.lanteam.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Equipment {
	
	private Long id;
	private OrderLine orderLine;
	private String serialNo;
	private String stealingTag;
	private Date creationDate;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne()
	public OrderLine getOrderLine() {
		return orderLine;
	}
	public void setOrderLine(OrderLine orderLine) {
		this.orderLine = orderLine;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getStealingTag() {
		return stealingTag;
	}
	public void setStealingTag(String stealingTag) {
		this.stealingTag = stealingTag;
	}
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
