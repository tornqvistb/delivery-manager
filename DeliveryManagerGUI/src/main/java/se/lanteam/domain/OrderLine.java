package se.lanteam.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

@Entity
public class OrderLine {

	private Long id;
	private Integer lineId;
	private String model;
	private Boolean hasSerialNo;
	private Integer price;
	private Integer total;
	private Integer registered;
	private Integer remaining;
	private OrderHeader orderHeader;
	private Set<Equipment> equipments = new HashSet<Equipment>();
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getLineId() {
		return lineId;
	}
	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Boolean getHasSerialNo() {
		return hasSerialNo;
	}
	public void setHasSerialNo(Boolean hasSerialNo) {
		this.hasSerialNo = hasSerialNo;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getRegistered() {
		return registered;
	}
	public void setRegistered(Integer registered) {
		this.registered = registered;
	}
	public Integer getRemaining() {
		return remaining;
	}
	public void setRemaining(Integer remaining) {
		this.remaining = remaining;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderLine")
	@OrderBy("creationDate")
	public Set<Equipment> getEquipments() {
		return equipments;
	}
	public void setEquipments(Set<Equipment> equipments) {
		this.equipments = equipments;
	}
	@ManyToOne()
	public OrderHeader getOrderHeader() {
		return orderHeader;
	}
	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}
	@Transient
	public String getOptionClass() {
		String result = "with-serial-no";
		if (!this.hasSerialNo) {
			result = "no-serial-no";
		}
		return result;
	}
	@Transient
	public String getHasSerialNoLabel() {
		String result = "Ja";
		if (!this.hasSerialNo) {
			result = "Nej";
		}
		return result;
	}
	
}
