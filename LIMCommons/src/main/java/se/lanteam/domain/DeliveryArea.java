package se.lanteam.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class DeliveryArea {

	private Long id;
	private String name;
	private DeliveryWeekDay deliveryWeekDay;
	private Set<DeliveryPlan> deliveryPlans = new HashSet<DeliveryPlan>();
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
	@ManyToOne
	public DeliveryWeekDay getDeliveryWeekDay() {
		return deliveryWeekDay;
	}

	public void setDeliveryWeekDay(DeliveryWeekDay deliveryWeekDay) {
		this.deliveryWeekDay = deliveryWeekDay;
	}
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "delivery_day_area", joinColumns = @JoinColumn(name = "delivery_area_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "delivery_week_day_id", referencedColumnName = "id"))	
	public Set<DeliveryPlan> getDeliveryPlans() {
		return deliveryPlans;
	}

	public void setDeliveryPlans(Set<DeliveryPlan> deliveryPlans) {
		this.deliveryPlans = deliveryPlans;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
