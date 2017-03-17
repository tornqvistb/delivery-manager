package se.lanteam.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private RegistrationConfig registrationConfig;
	private ReportsConfig reportsConfig;
	private Set<CustomerCustomField> customerCustomFields = new HashSet<CustomerCustomField>();

	public CustomerGroup() {
		super();
		creationDate = new Date();
	}
	
	@Id
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
	public Set<CustomerCustomField> getCustomerCustomFields() {
		return customerCustomFields;
	}

	public void setCustomerCustomFields(Set<CustomerCustomField> customerCustomFields) {
		this.customerCustomFields = customerCustomFields;
	}
	
}
