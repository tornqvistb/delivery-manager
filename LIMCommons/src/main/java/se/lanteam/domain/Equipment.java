package se.lanteam.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Equipment {
	
	private Long id;
	private OrderLine orderLine;
	private String serialNo;
	private String stealingTag;
	private boolean toCorrect = false;
	private String previousSerialNo;
	private String previousStealingTag;
	private Date creationDate;	
	private String registeredBy;
	@Transient
	private Long orderLineId;
	
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
	public boolean isToCorrect() {
		return toCorrect;
	}
	public void setToCorrect(boolean toCorrect) {
		this.toCorrect = toCorrect;
	}
	@Override
	public String toString() {
		return "Equipment [id=" + id + ", orderLine=" + orderLine + ", serialNo=" + serialNo + ", stealingTag="
				+ stealingTag + ", toCorrect=" + toCorrect + ", previousSerialNo=" + previousSerialNo
				+ ", previousStealingTag=" + previousStealingTag + ", creationDate=" + creationDate + ", orderLineId="
				+ orderLineId + "]";
	}
	public String getPreviousSerialNo() {
		return previousSerialNo;
	}
	public void setPreviousSerialNo(String previousSerialNo) {
		this.previousSerialNo = previousSerialNo;
	}
	public String getPreviousStealingTag() {
		return previousStealingTag;
	}
	public void setPreviousStealingTag(String previousStealingTag) {
		this.previousStealingTag = previousStealingTag;
	}
	public String getRegisteredBy() {
		return registeredBy;
	}
	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}
	@Transient
	public String getCreationDateDisplay() {		
		String result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(creationDate);
		return result;		
	}
}
