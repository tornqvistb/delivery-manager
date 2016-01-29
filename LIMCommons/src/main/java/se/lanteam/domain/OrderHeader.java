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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.thymeleaf.util.ArrayUtils;

import se.lanteam.constants.StatusConstants;
import se.lanteam.constants.StatusUtil;

@Entity
public class OrderHeader {

	private Long id;
	private String orderNumber;
	private Date orderDate;
	private String customerNumber;
	private String customerName;
	private String postalAddress1;
	private String postalAddress2;
	private String postalCode;
	private String city;
	private String deliveryPostalAddress1;
	private String deliveryPostalAddress2;
	private String deliveryPostalCode;
	private String deliveryCity;
	private String leasingNumber;	
	private String customerOrderNumber;
	private String customerSalesOrder;
	private String partnerId;
	private String contact1Name;
	private String contact1Email;
	private String contact1Phone;
	private String contact2Name;
	private String contact2Email;
	private String contact2Phone;	
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
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}
	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getPostalAddress1() {
		return postalAddress1;
	}
	public void setPostalAddress1(String postalAddress1) {
		this.postalAddress1 = postalAddress1;
	}
	public String getPostalAddress2() {
		return postalAddress2;
	}
	public void setPostalAddress2(String postalAddress2) {
		this.postalAddress2 = postalAddress2;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDeliveryPostalAddress1() {
		return deliveryPostalAddress1;
	}
	public void setDeliveryPostalAddress1(String deliveryPostalAddress1) {
		this.deliveryPostalAddress1 = deliveryPostalAddress1;
	}
	public String getDeliveryPostalAddress2() {
		return deliveryPostalAddress2;
	}
	public void setDeliveryPostalAddress2(String deliveryPostalAddress2) {
		this.deliveryPostalAddress2 = deliveryPostalAddress2;
	}
	public String getDeliveryPostalCode() {
		return deliveryPostalCode;
	}
	public void setDeliveryPostalCode(String deliveryPostalCode) {
		this.deliveryPostalCode = deliveryPostalCode;
	}
	public String getDeliveryCity() {
		return deliveryCity;
	}
	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}
	public String getLeasingNumber() {
		return leasingNumber;
	}
	public void setLeasingNumber(String leasingNumber) {
		this.leasingNumber = leasingNumber;
	}
	public String getCustomerSalesOrder() {
		return customerSalesOrder;
	}
	public void setCustomerSalesOrder(String customerSalesOrder) {
		this.customerSalesOrder = customerSalesOrder;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getContact1Name() {
		return contact1Name;
	}
	public void setContact1Name(String contact1Name) {
		this.contact1Name = contact1Name;
	}
	public String getContact1Email() {
		return contact1Email;
	}
	public void setContact1Email(String contact1Email) {
		this.contact1Email = contact1Email;
	}
	public String getContact1Phone() {
		return contact1Phone;
	}
	public void setContact1Phone(String contact1Phone) {
		this.contact1Phone = contact1Phone;
	}
	public String getContact2Name() {
		return contact2Name;
	}
	public void setContact2Name(String contact2Name) {
		this.contact2Name = contact2Name;
	}
	public String getContact2Email() {
		return contact2Email;
	}
	public void setContact2Email(String contact2Email) {
		this.contact2Email = contact2Email;
	}
	public String getContact2Phone() {
		return contact2Phone;
	}
	public void setContact2Phone(String contact2Phone) {
		this.contact2Phone = contact2Phone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderHeader")
	@OrderBy("rowNumber")
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
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderHeader", orphanRemoval=true)
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
	public String getStatusDisplay() {
		return StatusUtil.getOrderStatusDisplay(this.status);
	}
	@Transient
	public String getOrderDateAsString() {
		return String.valueOf(orderDate).substring(0, 10);
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
	
	@Transient
	public void setOrderStatusByProgress() {
		if (getUnCompletedOrderLines().size() > 0) {
			this.status = StatusConstants.ORDER_STATUS_STARTED;
		} else {
			if (this.attachment == null) {
				this.status = StatusConstants.ORDER_STATUS_REGISTRATION_DONE;
			} else {
				this.status = StatusConstants.ORDER_STATUS_SENT;
			}
		}
	}
	@Transient
	public Boolean getEditable() {
		Boolean result = false;
		if (ArrayUtils.contains(StatusConstants.ACTIVE_STATI, status)) {
			result = true;
		}
		return result;
	}
	@Transient
	public String getOrderSummary() {
		StringBuilder result = new StringBuilder();
		
		return result.toString();
	}
}
