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
	private OrderHeader header;
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
	@ManyToOne()
	public OrderHeader getHeader() {
		return header;
	}
	public void setHeader(OrderHeader header) {
		this.header = header;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderLine")
	public Set<Equipment> getEquipments() {
		return equipments;
	}
	public void setEquipments(Set<Equipment> equipments) {
		this.equipments = equipments;
	}
	
}
