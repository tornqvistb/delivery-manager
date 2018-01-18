package se.lanteam.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import se.lanteam.constants.CustomFieldConstants;

@Entity
public class OrderCustomField {
	
	private Long id;
	private CustomField customField;
	private OrderHeader orderHeader;
	private String value;
	private Date creationDate;

	
	
	public OrderCustomField(CustomField customField, OrderHeader orderHeader, String value, Date creationDate) {
		super();
		this.customField = customField;
		this.orderHeader = orderHeader;
		this.value = value;
		this.creationDate = creationDate;
	}

	public OrderCustomField() {
		super();
		creationDate = new Date();
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne()
	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}
	@ManyToOne()
	public OrderHeader getOrderHeader() {
		return orderHeader;
	}
	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@Transient
	public Boolean getShowInOrderDetails() {
		for (long id : CustomFieldConstants.CONTACT_FIELDS) {
			if (id == this.customField.getIdentification()) {
				return false;
			}
		}
		return true;
	}
	@Transient
	public String getLabel() {
		for (CustomerCustomField customField : this.orderHeader.getCustomerGroup().getCustomerCustomFields()) {
			if (customField.getCustomField().getIdentification() == this.customField.getIdentification()) {
				return customField.getLabel();
			}
		}
		return "";
	}
}
