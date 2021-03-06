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

@Entity
public class CustomerGroup {

	private Long id;
	private String name;
	private Date creationDate;
	private String emailAddress;
	private String backgroundColor;
	private RegistrationConfig registrationConfig;
	private ReportsConfig reportsConfig;
	private List<CustomerCustomField> customerCustomFields = new ArrayList<CustomerCustomField>();	
	private String ourReference;
	private Boolean sendDeliveryNotification;
	private Boolean getContactInfoFromNetset;
	private Boolean bookOrderBeforeRegistration;
	private Boolean deliveryFlagToERP;
	private String deliveryEmailAddress;
	private Boolean sendDeliveryMailToContacts;
	private Boolean autoRegisterInternalOrderLines;
	private Boolean allowPreDeliveryInfo;
	private Set<CustomerNumber> customerNumbers = new HashSet<CustomerNumber>();
	
	public String getDeliveryEmailAddress() {
		return deliveryEmailAddress;
	}

	public void setDeliveryEmailAddress(String deliveryEmailAddress) {
		this.deliveryEmailAddress = deliveryEmailAddress;
	}

	public Boolean getGetContactInfoFromNetset() {
		return getContactInfoFromNetset;
	}

	public void setGetContactInfoFromNetset(Boolean getContactInfoFromNetset) {
		this.getContactInfoFromNetset = getContactInfoFromNetset;
	}

	public CustomerGroup() {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="customerGroup", orphanRemoval=true)
	public RegistrationConfig getRegistrationConfig() {
		return registrationConfig;
	}
	public void setRegistrationConfig(RegistrationConfig registrationConfig) {
		this.registrationConfig = registrationConfig;
	}
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="customerGroup", orphanRemoval=true)
	public ReportsConfig getReportsConfig() {
		return reportsConfig;
	}
	public void setReportsConfig(ReportsConfig reportsConfig) {
		this.reportsConfig = reportsConfig;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="customerGroup", orphanRemoval=true)
	@OrderBy("id")
	public List<CustomerCustomField> getCustomerCustomFields() {
		return customerCustomFields;
	}
		
	public void setCustomerCustomFields(List<CustomerCustomField> customerCustomFields) {
		this.customerCustomFields = customerCustomFields;
	}

	public String getOurReference() {
		return ourReference;
	}

	public void setOurReference(String ourReference) {
		this.ourReference = ourReference;
	}

	public Boolean getSendDeliveryNotification() {
		return sendDeliveryNotification;
	}

	public void setSendDeliveryNotification(Boolean sendDeliveryNotification) {
		this.sendDeliveryNotification = sendDeliveryNotification;
	}

	public Boolean getBookOrderBeforeRegistration() {
		return bookOrderBeforeRegistration;
	}

	public void setBookOrderBeforeRegistration(Boolean bookOrderBeforeRegistration) {
		this.bookOrderBeforeRegistration = bookOrderBeforeRegistration;
	}

	public Boolean getDeliveryFlagToERP() {
		return deliveryFlagToERP;
	}

	public void setDeliveryFlagToERP(Boolean deliveryFlagToERP) {
		this.deliveryFlagToERP = deliveryFlagToERP;
	}

	public Boolean getSendDeliveryMailToContacts() {
		return sendDeliveryMailToContacts;
	}

	public void setSendDeliveryMailToContacts(Boolean sendDeliveryMailToContacts) {
		this.sendDeliveryMailToContacts = sendDeliveryMailToContacts;
	}

	public Boolean getAutoRegisterInternalOrderLines() {
		return autoRegisterInternalOrderLines;
	}

	public void setAutoRegisterInternalOrderLines(Boolean autoRegisterInternalOrderLines) {
		this.autoRegisterInternalOrderLines = autoRegisterInternalOrderLines;
	}

	public Boolean getAllowPreDeliveryInfo() {
		return allowPreDeliveryInfo;
	}

	public void setAllowPreDeliveryInfo(Boolean allowPreDeliveryInfo) {
		this.allowPreDeliveryInfo = allowPreDeliveryInfo;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="customerGroup", orphanRemoval=true)
	@OrderBy("id")
	public Set<CustomerNumber> getCustomerNumbers() {
		return customerNumbers;
	}

	public void setCustomerNumbers(Set<CustomerNumber> customerNumbers) {
		this.customerNumbers = customerNumbers;
	}

	
}
