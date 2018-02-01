package se.lanteam.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CustomerCustomField {

	private Long id;
	private Boolean showInSlaReport;
	private Boolean showInDeliveryReport;
	private Boolean showInDeliveryNote;
	private Boolean showInWorkNote;
	private CustomField customField;
	private CustomerGroup customerGroup;
	private Date creationDate;
	private String label;

	public CustomerCustomField(CustomField customField, CustomerGroup customerGroup) {
		super();
		this.showInSlaReport = false;
		this.showInDeliveryReport = false;
		this.showInDeliveryNote = false;
		this.showInWorkNote = false;
		this.customField = customField;
		this.customerGroup = customerGroup;
		this.creationDate = new Date();
		this.label = customField.getLabel();
	}

	public CustomerCustomField() {
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

	public Boolean getShowInDeliveryReport() {
		return showInDeliveryReport;
	}

	public void setShowInDeliveryReport(Boolean showInDeliveryReport) {
		this.showInDeliveryReport = showInDeliveryReport;
	}

	public Boolean getShowInDeliveryNote() {
		return showInDeliveryNote;
	}

	public void setShowInDeliveryNote(Boolean showInDeliveryNote) {
		this.showInDeliveryNote = showInDeliveryNote;
	}

	@ManyToOne()
	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

	@ManyToOne()
	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getShowInSlaReport() {
		return showInSlaReport;
	}

	public void setShowInSlaReport(Boolean showInSlaReport) {
		this.showInSlaReport = showInSlaReport;
	}

	public Boolean getShowInWorkNote() {
		return showInWorkNote;
	}

	public void setShowInWorkNote(Boolean showInWorkNote) {
		this.showInWorkNote = showInWorkNote;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
