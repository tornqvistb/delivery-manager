package se.lanteam.web;

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
	
	
}
