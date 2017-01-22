package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.SystemProperty;

public class RequestAttributes {

	private Long orderLineId;
	private String stealingTag;
	private String serialNo;
	private Integer total;
	private String comment;
	private String statusMessageCreationSuccess;
	private String statusMessageCreationFailed;
	private String statusAttachmentSuccess;
	private String statusAttachmentFailed;
	private String regEquipmentResult;
	private String orderStatus;
	private String errorStatus;
	private String thanksMessage;
	private List<ReqOrderLine> reqOrderLines= new ArrayList<ReqOrderLine>();	
	private Integer newErrorMessages;
	private List<SystemProperty> systemProperties= new ArrayList<SystemProperty>();
	private String query;
	private String errorMessage;
	private String infoMessage;
	private String registeredBy;
	private String fromDate;
	private String toDate;
	private String firstDate;
	private Integer activeCount;
	private Integer passiveCount;	
	private Long customerId;
	
	
	public RequestAttributes() {
		super();

	}
	public RequestAttributes(Integer newErrorMessages) {
		super();
		this.newErrorMessages = newErrorMessages;
	}

	public RequestAttributes(OrderHeader orderHeader) {
		super();
		List<ReqOrderLine> orderLines = new ArrayList<ReqOrderLine>();
		for (OrderLine line : orderHeader.getOrderLines()) {
			ReqOrderLine reqLine = new ReqOrderLine();
			for (Equipment equip : line.getEquipments()) {
				equip.setPreviousSerialNo(equip.getSerialNo());
				equip.setPreviousStealingTag(equip.getStealingTag());
				reqLine.getEquipments().add(equip);
			}
			orderLines.add(reqLine);
		}
		this.reqOrderLines = orderLines;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}

	public String getStealingTag() {
		return stealingTag;
	}

	public void setStealingTag(String stealingTag) {
		this.stealingTag = stealingTag;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatusMessageCreationSuccess() {
		return statusMessageCreationSuccess;
	}

	public void setStatusMessageCreationSuccess(String statusMessageCreationSuccess) {
		this.statusMessageCreationSuccess = statusMessageCreationSuccess;
		this.statusMessageCreationFailed = null;
	}

	public String getStatusMessageCreationFailed() {
		return statusMessageCreationFailed;
	}

	public void setStatusMessageCreationFailed(String statusMessageCreationFailed) {
		this.statusMessageCreationFailed = statusMessageCreationFailed;
		this.statusMessageCreationSuccess = null;
	}

	public String getStatusAttachmentSuccess() {
		return statusAttachmentSuccess;
	}

	public void setStatusAttachmentSuccess(String statusAttachmentSuccess) {
		this.statusAttachmentSuccess = statusAttachmentSuccess;
		this.statusAttachmentFailed = null;
	}

	public String getStatusAttachmentFailed() {
		return statusAttachmentFailed;
	}

	public void setStatusAttachmentFailed(String statusAttachmentFailed) {
		this.statusAttachmentFailed = statusAttachmentFailed;
		this.statusAttachmentSuccess = null;
	}

	public String getRegEquipmentResult() {
		return regEquipmentResult;
	}

	public void setRegEquipmentResult(String regEquipmentResult) {
		this.regEquipmentResult = regEquipmentResult;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}
	public boolean getShowNewErrors() {
		boolean result = true;
		if (StatusConstants.ERROR_STATUS_ARCHIVED.equals(this.errorStatus)) {
			result = false;
		}
		return result;
	}

	public List<ReqOrderLine> getReqOrderLines() {
		return reqOrderLines;
	}

	public void setReqOrderLines(List<ReqOrderLine> reqOrderLines) {
		this.reqOrderLines = reqOrderLines;
	}

	public String getThanksMessage() {
		return thanksMessage;
	}

	public void setThanksMessage(String thanksMessage) {
		this.thanksMessage = thanksMessage;
	}

	public Integer getNewErrorMessages() {
		return newErrorMessages;
	}

	public void setNewErrorMessages(Integer newErrorMessages) {
		this.newErrorMessages = newErrorMessages;
	}
	public List<SystemProperty> getSystemProperties() {
		return systemProperties;
	}
	public void setSystemProperties(List<SystemProperty> systemProperties) {
		this.systemProperties = systemProperties;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getRegisteredBy() {
		return registeredBy;
	}
	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}
	public String getInfoMessage() {
		return infoMessage;
	}
	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	public Integer getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}
	public Integer getPassiveCount() {
		return passiveCount;
	}
	public void setPassiveCount(Integer passiveCount) {
		this.passiveCount = passiveCount;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	
}
