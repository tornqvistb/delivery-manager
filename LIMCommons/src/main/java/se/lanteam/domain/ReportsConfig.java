package se.lanteam.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ReportsConfig {

	private Long id;
	private CustomerGroup customerGroup;
	private Boolean showContactPersons;
	private Boolean showDeliveryAddress;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne
	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}
	public Boolean getShowContactPersons() {
		return showContactPersons;
	}
	public void setShowContactPersons(Boolean showContactPersons) {
		this.showContactPersons = showContactPersons;
	}
	public Boolean getShowDeliveryAddress() {
		return showDeliveryAddress;
	}
	public void setShowDeliveryAddress(Boolean showDeliveryAddress) {
		this.showDeliveryAddress = showDeliveryAddress;
	}
	
}
