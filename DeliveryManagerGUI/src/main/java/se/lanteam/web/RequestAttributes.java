package se.lanteam.web;

public class RequestAttributes {

	private Long orderLineId;
	private String stealingTag;
	private String serialNo;
	private Integer total;

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
	
	
}
