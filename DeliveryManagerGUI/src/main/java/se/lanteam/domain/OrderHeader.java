package se.lanteam.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

@Entity
public class OrderHeader {

	private Long id;
	private String orderId;
	private String referenceId;
	private String description;
	private String customer;
	private Date creationDate;
	private String status;
	private Set<OrderLine> orderLines = new HashSet<OrderLine>();
	private Set<OrderComment> orderComments = new HashSet<OrderComment>();
	private Attachment attachment;
	private String transmitErrorMessage;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderHeader")
	@OrderBy("lineId")
	public Set<OrderLine> getOrderLines() {
		return orderLines;
	}
	public void setOrderLines(Set<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderHeader")
	@OrderBy("creationDate desc")
	public Set<OrderComment> getOrderComments() {
		return orderComments;
	}
	public void setOrderComments(Set<OrderComment> orderComments) {
		this.orderComments = orderComments;
	}
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderHeader")
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	public String getTransmitErrorMessage() {
		return transmitErrorMessage;
	}
	public void setTransmitErrorMessage(String transmitErrorMessage) {
		this.transmitErrorMessage = transmitErrorMessage;
	}
	@Transient
	public String getCreationDateAsString() {
		return String.valueOf(creationDate).substring(0, 10);
	}
	@Transient
	public List<OrderLine> getUnCompletedOrderLines() {
		List<OrderLine> result = new ArrayList<OrderLine>();
		for (OrderLine ol : this.orderLines) {
			if (ol.getRemaining() > 0) {
				result.add(ol);
			}
		}
		return result;
	}
}
