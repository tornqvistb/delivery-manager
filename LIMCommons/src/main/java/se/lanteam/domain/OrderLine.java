package se.lanteam.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import org.springframework.util.StringUtils;
import org.thymeleaf.util.ArrayUtils;

import se.lanteam.constants.RestrictionCodes;
import se.lanteam.constants.StatusConstants;

@Entity
public class OrderLine implements Cloneable{

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
	private Integer customerRowNumber;
	private String organisationUnit;
	private String installationType;
	private String operatingSystem;
	private String leasingNumber;
	private String requestItemNumber;
	private boolean autoRegistered = false;
	private boolean rested = false;
	private String shopGroupId;
	private String shopId;
	private String shopType;
	
	@Transient
    public OrderLine cloneToRestOrderLine() {		
		try {
			OrderLine clone = (OrderLine) super.clone();
			clone.setId(null);
			clone.setEquipments(new HashSet<Equipment>());
			clone.setRegistered(0);
			clone.setRemaining(this.getRemaining());
			clone.setOrderHeader(null);
			clone.setRested(false);
			clone.setAutoRegistered(false);
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
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
		if (this.restrictionCode.equals(RestrictionCodes.NO_SLA_SERIALN0) || this.restrictionCode.equals(RestrictionCodes.SLA_SERIALN0) || this.restrictionCode.equals(RestrictionCodes.NO_SLA_SERIALN0_NO_STEALING_TAG)) {
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
			if (isDistArticle()) {
				result = "distributed-row";
			}
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
	@Transient
	public Boolean isManualRegistrationLeft() {
		for (Equipment equip : equipments) {
			if (StringUtils.isEmpty(equip.getStealingTag()) && !RestrictionCodes.NO_SLA_SERIALN0_NO_STEALING_TAG.equals(this.restrictionCode)) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public Boolean isFullyRegistered() {
		return registered >= total;
	}
	
	@Transient
	public Boolean getEditable() {
		return this.orderHeader.getEditable() && !RestrictionCodes.NO_SLA_SERIALN0_NO_STEALING_TAG.equals(this.restrictionCode);
	}
	
	public Integer getCustomerRowNumber() {
		return customerRowNumber;
	}
	public void setCustomerRowNumber(Integer customerRowNumber) {
		this.customerRowNumber = customerRowNumber;
	}

	public String getOrganisationUnit() {
		return organisationUnit;
	}
	public void setOrganisationUnit(String organisationUnit) {
		this.organisationUnit = organisationUnit;
	}
	public String getInstallationType() {
		return installationType;
	}
	public void setInstallationType(String installationType) {
		this.installationType = installationType;
	}
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	@Transient
	public void updateEquipmentCounters() {
		this.setRegistered(this.getEquipments().size());
		this.setRemaining(this.getTotal() - this.getEquipments().size());
	}
	public String getLeasingNumber() {
		return leasingNumber;
	}
	public void setLeasingNumber(String leasingNumber) {
		this.leasingNumber = leasingNumber;
	}
	public String getRequestItemNumber() {
		return requestItemNumber;
	}
	public void setRequestItemNumber(String requestItemNumber) {
		this.requestItemNumber = requestItemNumber;
	}
	public boolean isAutoRegistered() {
		return autoRegistered;
	}
	public void setAutoRegistered(boolean autoRegistered) {
		this.autoRegistered = autoRegistered;
	}
	@Transient
	public void addPickedQuantity(int qty) {
		this.registered = this.registered + qty;
		this.remaining = this.remaining - qty;
	}

	@Transient
	public boolean isCustomerOrderLine() {
		return !StringUtils.isEmpty(shopId) && !StringUtils.isEmpty(shopGroupId) && shopId.equals(shopGroupId);
	}

	@Transient
	public boolean isDistArticle() {
		return StringUtils.isEmpty(restrictionCode);
	}
	
	@Transient
	public void addPickedSerialNumbers(List<String> serialNos) {
		for (String serialNo : serialNos) {
			Equipment eq = new Equipment();
			eq.setSerialNo(serialNo);
			eq.setCreationDate(new Date());
			eq.setOrderLine(this);
			this.getEquipments().add(eq);
		}
		this.registered = this.registered + serialNos.size();
		this.remaining = this.remaining - serialNos.size();
	}
	
	public boolean isRested() {
		return rested;
	}

	public void setRested(boolean rested) {
		this.rested = rested;
	}

	public String getShopGroupId() {
		return shopGroupId;
	}

	public void setShopGroupId(String shopGroupId) {
		this.shopGroupId = shopGroupId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}
}
