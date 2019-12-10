package se.lanteam.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CustomerNumber {

	private Long id;
	private String number;
	private String label;
	private CustomerGroup customerGroup;
	
	
	public CustomerNumber() {
		super();
	}

	public CustomerNumber(String number, String label, CustomerGroup customerGroup) {
		super();
		this.number = number;
		this.label = label;
		this.customerGroup = customerGroup;
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@ManyToOne()
	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}
	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}
}