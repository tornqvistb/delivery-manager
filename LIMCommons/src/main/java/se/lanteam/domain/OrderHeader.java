package se.lanteam.domain;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;
import org.thymeleaf.util.ArrayUtils;

import se.lanteam.constants.CustomFieldConstants;
import se.lanteam.constants.DateUtil;
import se.lanteam.constants.SLAConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.constants.StatusUtil;

@Entity
public class OrderHeader {

	private Long id;
	private String orderNumber;
	private String netsetOrderNumber;
	private Date orderDate;
	private String customerNumber;
	private String customerName;
	private String postalAddress1;
	private String postalAddress2;
	private String postalCode;
	private String city;
	private String deliveryAddressName;
	private String deliveryPostalAddress1;
	private String deliveryPostalAddress2;
	private String deliveryPostalCode;
	private String deliveryCity;
	private Date deliveryDate;
	private String leasingNumber;	
	private String customerOrderNumber;
	private String customerSalesOrder;
	private String partnerId;
	private String contact1Name;
	private String contact1Email;
	private String contact1Phone;
	private String contact2Name;
	private String contact2Email;
	private String contact2Phone;	
	private String status;
	private Set<OrderLine> orderLines = new HashSet<OrderLine>();
	private Set<OrderComment> orderComments = new HashSet<OrderComment>();
	private Set<OrderCustomField> orderCustomFields = new HashSet<OrderCustomField>();
	private Attachment attachment;
	private String transmitErrorMessage;
	private Boolean toBeArchived = false;
	private CustomerGroup customerGroup;
	private DeliveryPlan deliveryPlan;
	private String jointDelivery;
	private String jointDeliveryText;
	private String jointDeliveryOrders;
	private int jointInvoicing;
	private String articleNumbers;
	private Boolean receivedFromWebshop = false;
	private Boolean receivedFromERP = false;
	private Boolean contactInfoFromNetset = false;
	private Integer slaDays;
	private Boolean excludeFromList = false;
	private Date creationDate = new Date();
	@Transient
	private List<OrderCustomField> customFieldsInDeliveryNote = new ArrayList<OrderCustomField>();
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getNetsetOrderNumber() {
		return netsetOrderNumber;
	}
	public void setNetsetOrderNumber(String netsetOrderNumber) {
		this.netsetOrderNumber = netsetOrderNumber;
	}
	public String getCustomerOrderNumber() {
		return customerOrderNumber;
	}
	public void setCustomerOrderNumber(String customerOrderNumber) {
		this.customerOrderNumber = customerOrderNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getPostalAddress1() {
		return postalAddress1;
	}
	public void setPostalAddress1(String postalAddress1) {
		this.postalAddress1 = postalAddress1;
	}
	public String getPostalAddress2() {
		return postalAddress2;
	}
	public void setPostalAddress2(String postalAddress2) {
		this.postalAddress2 = postalAddress2;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDeliveryAddressName() {
		return deliveryAddressName;
	}
	public void setDeliveryAddressName(String deliveryAddressName) {
		this.deliveryAddressName = deliveryAddressName;
	}
	public String getDeliveryPostalAddress1() {
		return deliveryPostalAddress1;
	}
	public void setDeliveryPostalAddress1(String deliveryPostalAddress1) {
		this.deliveryPostalAddress1 = deliveryPostalAddress1;
	}
	public String getDeliveryPostalAddress2() {
		return deliveryPostalAddress2;
	}
	public void setDeliveryPostalAddress2(String deliveryPostalAddress2) {
		this.deliveryPostalAddress2 = deliveryPostalAddress2;
	}
	public String getDeliveryPostalCode() {
		return deliveryPostalCode;
	}
	public void setDeliveryPostalCode(String deliveryPostalCode) {
		this.deliveryPostalCode = deliveryPostalCode;
	}
	public String getDeliveryCity() {
		return deliveryCity;
	}
	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}
	public String getLeasingNumber() {
		return leasingNumber;
	}
	public void setLeasingNumber(String leasingNumber) {
		this.leasingNumber = leasingNumber;
	}
	public String getCustomerSalesOrder() {
		return customerSalesOrder;
	}
	public void setCustomerSalesOrder(String customerSalesOrder) {
		this.customerSalesOrder = customerSalesOrder;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getContact1Name() {
		return contact1Name;
	}
	public void setContact1Name(String contact1Name) {
		this.contact1Name = contact1Name;
	}
	public String getContact1Email() {
		return contact1Email;
	}
	public void setContact1Email(String contact1Email) {
		this.contact1Email = contact1Email;
	}
	public String getContact1Phone() {
		return contact1Phone;
	}
	public void setContact1Phone(String contact1Phone) {
		this.contact1Phone = contact1Phone;
	}
	public String getContact2Name() {
		return contact2Name;
	}
	public void setContact2Name(String contact2Name) {
		this.contact2Name = contact2Name;
	}
	public String getContact2Email() {
		return contact2Email;
	}
	public void setContact2Email(String contact2Email) {
		this.contact2Email = contact2Email;
	}
	public String getContact2Phone() {
		return contact2Phone;
	}
	public void setContact2Phone(String contact2Phone) {
		this.contact2Phone = contact2Phone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="orderHeader")
	@OrderBy("rowNumber")	
	public Set<OrderLine> getOrderLines() {
		return orderLines;
	}
	public void setOrderLines(Set<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="orderHeader")
	@OrderBy("creationDate desc")
	public Set<OrderComment> getOrderComments() {
		return orderComments;
	}
	public void setOrderComments(Set<OrderComment> orderComments) {
		this.orderComments = orderComments;
	}
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="orderHeader", orphanRemoval=true)
	public Attachment getAttachment() {
		return attachment;
	}
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	public String getTransmitErrorMessage() {
		return transmitErrorMessage;
	}
	public void setTransmitErrorMessage(String transmitErrorMessage) {
		this.transmitErrorMessage = transmitErrorMessage;
	}
	@Transient
	public String getStatusDisplay() {
		return StatusUtil.getOrderStatusDisplay(this.status);
	}
	@Transient
	public String getOrderDateAsString() {
		return DateUtil.dateToString(orderDate);
		//return String.valueOf(orderDate).substring(0, 10);
	}
	@Transient
	public List<OrderLine> getUnCompletedOrderLines() {
		List<OrderLine> result = new ArrayList<OrderLine>();
		for (OrderLine ol : this.orderLines) {
			if (ol.getRemaining() > 0) {
				result.add(ol);
			}
		}
		return result;
	}
	
	@Transient
	public void setOrderStatusByProgress(boolean workToDoOnRelatedOrders) {
		if (getUnCompletedOrderLines().size() > 0) {
			this.status = StatusConstants.ORDER_STATUS_STARTED;
		} else if (!workToDoOnRelatedOrders) {
			if (this.attachment == null) {
				if (this.customerGroup.getBookOrderBeforeRegistration()) {
					this.status = StatusConstants.ORDER_STATUS_ROUTE_PLANNED;
				} else {
					this.status = StatusConstants.ORDER_STATUS_REGISTRATION_DONE;
				}
			} else {
				this.status = StatusConstants.ORDER_STATUS_SENT;
			}
		}
	}
	@Transient
	public Boolean getEditable() {
		Boolean result = false;
		if (ArrayUtils.contains(StatusConstants.ACTIVE_STATI, status)) {
			result = true;
		}
		return result;
	}
	@Transient
	public Boolean getStatusMessageAllowed() {
		Boolean result = false;
		if (this.getCustomerGroup().getSendDeliveryNotification() && this.getEditable()) {
			result = true;
		}
		return result;
	}	
	@Transient
	public String getRoutePlanSummary() {
		String result = "";
		if (this.deliveryPlan != null) {
			result = "Denna order är ruttplanerad till " + DateUtil.dateToString(this.deliveryPlan.getPlannedDeliveryDate()) + ", område " + this.deliveryPlan.getDeliveryArea().getName() + "."; 
		} else if (!getPlannable()){
			result = "Ordern kan endast ruttplaneras då statusen är " + StatusConstants.ORDER_STATUS_REGISTRATION_DONE_DISP + ".";
		}
		return result;
	}
	@Transient
	public Boolean getRoutePlanEditable() {
		Boolean result = false;
		if (StatusConstants.ORDER_STATUS_ROUTE_PLANNED.equals(status) || StatusConstants.ORDER_STATUS_BOOKED.equals(status)) {
			result = true;
		}
		return result;		
	}
	@Transient
	public Boolean getPlannable() {
		Boolean result = false;
		if (StatusConstants.ORDER_STATUS_REGISTRATION_DONE.equals(status)
				|| (StatusConstants.ORDER_STATUS_NEW.equals(status) && this.customerGroup.getBookOrderBeforeRegistration())) {
			result = true;			
		}
		return result;
	}
	@Transient
	public String getOrderSummary() {
		StringBuilder result = new StringBuilder();
		if (this.status.equals(StatusConstants.ORDER_STATUS_RECEIVING)) {
			result.append("Ordern är delvis mottagen i LIM. Registrering kan påbörjas först när ordern har tagit emot information från samtliga system.");
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_NEW)) {
			result.append("Registrering ej påbörjad.");
			if (this.getCustomerGroup().getBookOrderBeforeRegistration()) {
				result.append("<br>Ordern måste bokas innan registrering kan påbörjas. Bokning sker genom ruttplanering.");
			}
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_STARTED)) {
			result.append("Registrering påbörjad.");
			if (this.attachment == null) {
				result.append("<br>Leveransdokument ej bifogat.");
			} else {
				result.append("<br>Leveransdokument bifogat.");
			}
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_REGISTRATION_DONE)) {
			result.append("Registrering klar.");
			result.append("<br>Leveransdokument ej bifogat.");			
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_SENT)) {
			result.append("Registrering klar. Leveransrapportering pågår.");			
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_TRANSFERED)) {
			result.append("Leveransrapportering klar och överförd till kund.");			
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_BOOKED)) {
			result.append("Ordern är bokad. Registrering av utrustning kan nu påbörjas.");
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_ROUTE_PLANNED)) {
			result.append("Ordern är ruttplanerad och inväntar nu leveransdokument.");
		} else if (this.status.equals(StatusConstants.ORDER_STATUS_NOT_ACCEPTED)) {
			result.append("Orderleveransen har ej accepterats av kunden.");
		}
		return result.toString();
	}
	@Transient
	public Boolean getOrderCanBeCorrected() {
		Boolean result = false;
		if (this.status.equals(StatusConstants.ORDER_STATUS_TRANSFERED) || 
			this.status.equals(StatusConstants.ORDER_STATUS_NOT_ACCEPTED)) {
			result = true;
		}
		return result;
	}
	@Transient
	public String getPrintMessage() {
		String result = "Här kan du skriva ut etiketter för denna order. Ordern innehåller <span class='big-fat'>#1</span> enheter med serienummer.";
		Integer count = 0;
		for (OrderLine line : this.orderLines) {
			if (line.getHasSerialNo()) {
				count = count + line.getTotal();
			}
		}
		result = result.replaceFirst("#1", String.valueOf(count));
		return result;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	@Transient
	public String getDeliveryDateDisplay() {
		String result = "";
		if (this.deliveryDate != null) {
			result = new SimpleDateFormat("yyyy-MM-dd").format(deliveryDate);
		}
		return result;
	}
	@Transient
	public String getDeliveryTimeDisplay() {
		String result = "";
		if (this.deliveryDate != null) {
			result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(deliveryDate);
		}
		return result;
	}
	@Transient
	public String getGroupStatus() {
		String result = StatusConstants.ORDER_STATUS_GROUP_ACTIVE;
		if (this.status != null) {
			if (Arrays.asList(StatusConstants.ORDER_STATUS_GROUP_ACTIVE).contains(status)) {
				result = StatusConstants.ORDER_STATUS_GROUP_ACTIVE;
			} else {
				result = StatusConstants.ORDER_STATUS_GROUP_INACTIVE;
			}
		}
		return result;
	}
	public Boolean getToBeArchived() {
		return toBeArchived;
	}
	public void setToBeArchived(Boolean toBeArchived) {
		this.toBeArchived = toBeArchived;
	}
	@ManyToOne()
	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}
	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="orderHeader", orphanRemoval=true)
	public DeliveryPlan getDeliveryPlan() {
		return deliveryPlan;
	}
	public void setDeliveryPlan(DeliveryPlan deliveryPlan) {
		this.deliveryPlan = deliveryPlan;
	}
	@Transient
	public Integer getSlaDaysLeft() {
		LocalDate endDate;
		
		if (this.deliveryDate == null) {
			endDate = LocalDate.now();
		} else {
			endDate = deliveryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		Integer slaDaysForOrder = this.slaDays;
		if (slaDaysForOrder == null) slaDaysForOrder = SLAConstants.SLA_LONG;
		LocalDate startDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate slaDate = DateUtil.addWorkingDays(startDate, slaDaysForOrder);
		return DateUtil.getWorkingDaysBetweenDates(endDate, slaDate);
	}
	@Transient
	public String getSlaDisplayClass() {
		String theClass = "";
		if (this.getSlaDaysLeft() < 0) {
			theClass = "late-order";
		}
		return theClass;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="orderHeader")
	@OrderBy("creationDate")
	public Set<OrderCustomField> getOrderCustomFields() {
		return orderCustomFields;
	}
	public void setOrderCustomFields(Set<OrderCustomField> orderCustomFields) {
		this.orderCustomFields = orderCustomFields;
	}
	public String getJointDelivery() {
		return jointDelivery;
	}
	public void setJointDelivery(String jointDelivery) {
		this.jointDelivery = jointDelivery;
	}
	public String getJointDeliveryText() {
		return jointDeliveryText;
	}
	public void setJointDeliveryText(String jointDeliveryText) {
		this.jointDeliveryText = jointDeliveryText;
	}
	public String getJointDeliveryOrders() {
		return jointDeliveryOrders;
	}
	public void setJointDeliveryOrders(String jointDeliveryOrders) {
		this.jointDeliveryOrders = jointDeliveryOrders;
	}
	public int getJointInvoicing() {
		return jointInvoicing;
	}
	public void setJointInvoicing(int jointInvoicing) {
		this.jointInvoicing = jointInvoicing;
	}
	public String getArticleNumbers() {
		return articleNumbers;
	}
	public void setArticleNumbers(String articleNumbers) {
		this.articleNumbers = articleNumbers;
	}
	public Boolean getReceivedFromWebshop() {
		return receivedFromWebshop;
	}
	public void setReceivedFromWebshop(Boolean receivedFromWebshop) {
		this.receivedFromWebshop = receivedFromWebshop;
	}
	public Boolean getReceivedFromERP() {
		return receivedFromERP;
	}
	public void setReceivedFromERP(Boolean receivedFromERP) {
		this.receivedFromERP = receivedFromERP;
	}
	public Integer getSlaDays() {
		return slaDays;
	}
	public void setSlaDays(Integer slaDays) {
		this.slaDays = slaDays;
	}
	@Transient
	public String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	public Boolean getExcludeFromList() {
		return excludeFromList;
	}
	public void setExcludeFromList(Boolean excludeFromList) {
		this.excludeFromList = excludeFromList;
	}
	@Transient
	public List<OrderCustomField> getCustomFieldsInDeliveryNote() {
		return customFieldsInDeliveryNote;
	}
	@Transient
	public void setCustomFieldsInDeliveryNote(List<OrderCustomField> customFieldsInDeliveryNote) {
		this.customFieldsInDeliveryNote = customFieldsInDeliveryNote;
	}
	@Transient
	public void setReceivingStatus() {
		if (this.receivedFromERP && this.receivedFromWebshop) {
			this.status = StatusConstants.ORDER_STATUS_NEW;
		} else {
			this.status = StatusConstants.ORDER_STATUS_RECEIVING;
		}
	}
	@Transient
	public Boolean getOkToRegister() {
		Boolean result = false;
		if (this.getUnCompletedOrderLines().size() > 0
				&& this.receivedFromWebshop != null && this.receivedFromWebshop
				&& this.receivedFromERP != null && this.receivedFromERP) {
			result = true;
			// Check if order must be booked before registration
			if (this.customerGroup.getBookOrderBeforeRegistration()) {
				if (this.status.equals(StatusConstants.ORDER_STATUS_BOOKED) || this.status.equals(StatusConstants.ORDER_STATUS_STARTED)) {
					result = true;
				} else {
					result = false;
				}
			}
		} else {
			result = false;
		}
		return result;
	}
	@Transient
	public boolean isPartOfJointdelivery() {
		boolean result = false;
		if (!StringUtils.isEmpty(this.jointDelivery)) {
			result = true;
		}
		return result;
	}
	@Transient
	public boolean isMainOrderInJoint() {
		boolean result = false;
		if (isPartOfJointdelivery() && this.jointDelivery.equalsIgnoreCase(CustomFieldConstants.VALUE_SAMLEVERANS_MASTER)) {
			result = true;
		}
		return result;
	}
	@Transient
	public boolean isChildOrderInJoint() {
		return isPartOfJointdelivery() && !isMainOrderInJoint();
	}
	
	public Boolean getContactInfoFromNetset() {
		return contactInfoFromNetset;
	}
	public void setContactInfoFromNetset(Boolean contactInfoFromNetset) {
		this.contactInfoFromNetset = contactInfoFromNetset;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/*
	@Transient
	public boolean isOriginateFromServiceNow() {
		return (customerOrderNumber != null
				&& customerOrderNumber.contains("REQ")
				&& customerOrderNumber.contains("RITM")
				&& customerOrderNumber.contains("TASK"));
	}
	*/
	@Transient
	public boolean isOriginateFromServiceNow() {
		return (leasingNumber != null
				&& leasingNumber.lastIndexOf('/') > 0);
	}
	@Transient
	public String getRequestNumber() {
		String result = "";
		if (customerOrderNumber != null
				&& customerOrderNumber.startsWith("REQ")) {
			int endpos = customerOrderNumber.indexOf('.');
			if (endpos > 0) {
				result = customerOrderNumber.substring(0, endpos);
			}
		}
		return result;
	}
	@Transient
	public int getTotalItemsForSNOrder() {
		int result = 1;
		if (leasingNumber != null) {
			int startPos = leasingNumber.lastIndexOf('/');
			if (startPos > 0) {
				try {
					result = Integer.parseInt(leasingNumber.substring(startPos + 1));
				} catch (NumberFormatException e) {
					result = 1;
				}
			}
		}
		return result;
	}
	
}
