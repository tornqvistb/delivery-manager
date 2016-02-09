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
	private Integer rowNumber;
	private String articleNumber;
	private String articleDescription;
	private Integer total;
	private String restrictionCode;
	private Integer registered = 0;
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
	public Integer getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	public String getArticleDescription() {
		return articleDescription;
	}
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getRestrictionCode() {
		return restrictionCode;
	}
	public void setRestrictionCode(String restrictionCode) {
		this.restrictionCode = restrictionCode;
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
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderLine", orphanRemoval=true)
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
	public Boolean getHasSerialNo() {
		Boolean result = false;
		if (this.restrictionCode.equals("1") || this.restrictionCode.equals("3")) {
			result = true;
		}
		return result;
	}
	@Transient
	public String getOptionClass() {
		String result = "no-serial-no";
		if (getHasSerialNo()) {
			result = "with-serial-no";
		}
		return result;
	}
	@Transient
	public String getHasSerialNoLabel() {
		String result = "Nej";
		if (getHasSerialNo()) {
			result = "Ja";
		}
		return result;
	}
	@Transient
	public String getRowClass() {
		String result = "";
		if (getRemaining() == 0) {
			result = "completed-row";
		}
		return result;
	}
	@Transient
	public Boolean getAnyEquipmentToCorrect() {
		Boolean result = false;
		for (Equipment equip : equipments) {
			if (equip.isToCorrect()) {
				result = true;
				break;
			}
		}
		return result;
	}
	
}
