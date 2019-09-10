package se.lanteam.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ReportsConfig {

	private Long id;
	private CustomerGroup customerGroup;
	private Boolean showOrderNumber;
	private Boolean showNetsetOrderNumber;
	private Boolean showCustomerCity;
	private Boolean showContactPerson1;
	private Boolean showContactPerson2;
	private Boolean showCustomerName;
	private Boolean showCustomerNumber;
	private Boolean showCustomerOrderNumber;
	private Boolean showCustomerSalesOrder;
	private Boolean showDeliveryAddress;
	private Boolean showLeasingNumber;
		
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
	public Boolean getShowOrderNumber() {
		return showOrderNumber;
	}
	public void setShowOrderNumber(Boolean showOrderNumber) {
		this.showOrderNumber = showOrderNumber;
	}
	public Boolean getShowNetsetOrderNumber() {
		return showNetsetOrderNumber;
	}
	public void setShowNetsetOrderNumber(Boolean showNetsetOrderNumber) {
		this.showNetsetOrderNumber = showNetsetOrderNumber;
	}
	public Boolean getShowCustomerCity() {
		return showCustomerCity;
	}
	public void setShowCustomerCity(Boolean showCustomerCity) {
		this.showCustomerCity = showCustomerCity;
	}
	public Boolean getShowContactPerson1() {
		return showContactPerson1;
	}
	public void setShowContactPerson1(Boolean showContactPerson1) {
		this.showContactPerson1 = showContactPerson1;
	}
	public Boolean getShowContactPerson2() {
		return showContactPerson2;
	}
	public void setShowContactPerson2(Boolean showContactPerson2) {
		this.showContactPerson2 = showContactPerson2;
	}
	public Boolean getShowCustomerName() {
		return showCustomerName;
	}
	public void setShowCustomerName(Boolean showCustomerName) {
		this.showCustomerName = showCustomerName;
	}
	public Boolean getShowCustomerNumber() {
		return showCustomerNumber;
	}
	public void setShowCustomerNumber(Boolean showCustomerNumber) {
		this.showCustomerNumber = showCustomerNumber;
	}
	public Boolean getShowCustomerOrderNumber() {
		return showCustomerOrderNumber;
	}
	public void setShowCustomerOrderNumber(Boolean showCustomerOrderNumber) {
		this.showCustomerOrderNumber = showCustomerOrderNumber;
	}
	public Boolean getShowCustomerSalesOrder() {
		return showCustomerSalesOrder;
	}
	public void setShowCustomerSalesOrder(Boolean showCustomerSalesOrder) {
		this.showCustomerSalesOrder = showCustomerSalesOrder;
	}
	public Boolean getShowDeliveryAddress() {
		return showDeliveryAddress;
	}
	public void setShowDeliveryAddress(Boolean showDeliveryAddress) {
		this.showDeliveryAddress = showDeliveryAddress;
	}
	public Boolean getShowLeasingNumber() {
		return showLeasingNumber;
	}
	public void setShowLeasingNumber(Boolean showLeasingNumber) {
		this.showLeasingNumber = showLeasingNumber;
	}
	
}
