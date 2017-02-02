package se.lanteam.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class DeliveryArea {

	private Long id;
	private String name;
	private Set<DeliveryWeekDay> deliveryWeekDays;
	private Date creationDate;

	public DeliveryArea() {
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
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "delivery_day_area", joinColumns = @JoinColumn(name = "delivery_area_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "delivery_week_day_id", referencedColumnName = "id"))	
	public Set<DeliveryWeekDay> getDeliveryWeekDays() {
		return deliveryWeekDays;
	}

	public void setDeliveryWeekDays(Set<DeliveryWeekDay> deliveryWeekDays) {
		this.deliveryWeekDays = deliveryWeekDays;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
