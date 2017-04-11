package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.CustomerCustomField;
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
	private String customAttribute1;
	private String customAttribute2;
	private String customAttribute3;
	private String customAttribute4;
	private String customAttribute5;
	private String customAttribute6;
	private String customAttribute7;
	private String customAttribute8;
	private Long zeroValue = 0L;
	private String fromOrderNo;
	private String toOrderNo;
	private String deliveryAreaId;
	private String deliveryDayId;
	private String deliveryDate;
	private String statusRouteplanSuccess;
	private String statusRouteplanFailed;
	private String planDate;
	private String resultNotEmptyMsg;
	private String resultEmptyMsg;
	private List<CustomerCustomField> customerCustomFields = new ArrayList<CustomerCustomField>();
	
	public RequestAttributes() {
		super();

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

	public String getCustomAttribute1() {
		return customAttribute1;
	}

	public void setCustomAttribute1(String customAttribute1) {
		this.customAttribute1 = customAttribute1;
	}

	public String getCustomAttribute2() {
		return customAttribute2;
	}

	public void setCustomAttribute2(String customAttribute2) {
		this.customAttribute2 = customAttribute2;
	}

	public String getCustomAttribute3() {
		return customAttribute3;
	}

	public void setCustomAttribute3(String customAttribute3) {
		this.customAttribute3 = customAttribute3;
	}

	public String getCustomAttribute4() {
		return customAttribute4;
	}

	public void setCustomAttribute4(String customAttribute4) {
		this.customAttribute4 = customAttribute4;
	}

	public String getCustomAttribute5() {
		return customAttribute5;
	}

	public void setCustomAttribute5(String customAttribute5) {
		this.customAttribute5 = customAttribute5;
	}

	public String getCustomAttribute6() {
		return customAttribute6;
	}

	public void setCustomAttribute6(String customAttribute6) {
		this.customAttribute6 = customAttribute6;
	}

	public String getCustomAttribute7() {
		return customAttribute7;
	}

	public void setCustomAttribute7(String customAttribute7) {
		this.customAttribute7 = customAttribute7;
	}

	public String getCustomAttribute8() {
		return customAttribute8;
	}

	public void setCustomAttribute8(String customAttribute8) {
		this.customAttribute8 = customAttribute8;
	}

	public Long getZeroValue() {
		return zeroValue;
	}

	public void setZeroValue(Long zeroValue) {
		this.zeroValue = zeroValue;
	}

	public String getFromOrderNo() {
		return fromOrderNo;
	}

	public void setFromOrderNo(String fromOrderNo) {
		this.fromOrderNo = fromOrderNo;
	}

	public String getToOrderNo() {
		return toOrderNo;
	}

	public void setToOrderNo(String toOrderNo) {
		this.toOrderNo = toOrderNo;
	}

	public String getDeliveryAreaId() {
		return deliveryAreaId;
	}

	public void setDeliveryAreaId(String deliveryAreaId) {
		this.deliveryAreaId = deliveryAreaId;
	}

	public String getDeliveryDayId() {
		return deliveryDayId;
	}

	public void setDeliveryDayId(String deliveryDayId) {
		this.deliveryDayId = deliveryDayId;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getStatusRouteplanSuccess() {
		return statusRouteplanSuccess;
	}

	public void setStatusRouteplanSuccess(String statusRouteplanSuccess) {
		this.statusRouteplanSuccess = statusRouteplanSuccess;
	}

	public String getStatusRouteplanFailed() {
		return statusRouteplanFailed;
	}

	public void setStatusRouteplanFailed(String statusRouteplanFailed) {
		this.statusRouteplanFailed = statusRouteplanFailed;
	}

	public String getPlanDate() {
		return planDate;
	}

	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}

	public String getResultNotEmptyMsg() {
		return resultNotEmptyMsg;
	}

	public void setResultNotEmptyMsg(String resultNotEmptyMsg) {
		this.resultNotEmptyMsg = resultNotEmptyMsg;
	}

	public String getResultEmptyMsg() {
		return resultEmptyMsg;
	}

	public void setResultEmptyMsg(String resultEmptyMsg) {
		this.resultEmptyMsg = resultEmptyMsg;
	}

	public List<CustomerCustomField> getCustomerCustomFields() {
		return customerCustomFields;
	}

	public void setCustomerCustomFields(List<CustomerCustomField> customerCustomFields) {
		this.customerCustomFields = customerCustomFields;
	}
	
}
