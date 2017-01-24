package se.lanteam.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Equipment {
	
	private Long id;
	private OrderLine orderLine;
	private String serialNo;
	private String stealingTag;
	private boolean toCorrect = false;
	private String previousSerialNo;
	private String previousStealingTag;
	private Date creationDate;	
	private String registeredBy;
	private String customAttribute1;
	private String customAttribute2;
	private String customAttribute3;
	private String customAttribute4;
	private String customAttribute5;
	private String customAttribute6;
	private String customAttribute7;
	private String customAttribute8;
	private String customAttribute1Label;
	private String customAttribute2Label;
	private String customAttribute3Label;
	private String customAttribute4Label;
	private String customAttribute5Label;
	private String customAttribute6Label;
	private String customAttribute7Label;
	private String customAttribute8Label;
	@Transient
	private Long orderLineId;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne()
	public OrderLine getOrderLine() {
		return orderLine;
	}
	public void setOrderLine(OrderLine orderLine) {
		this.orderLine = orderLine;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getStealingTag() {
		return stealingTag;
	}
	public void setStealingTag(String stealingTag) {
		this.stealingTag = stealingTag;
	}
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public boolean isToCorrect() {
		return toCorrect;
	}
	public void setToCorrect(boolean toCorrect) {
		this.toCorrect = toCorrect;
	}
	@Override
	public String toString() {
		return "Equipment [id=" + id + ", orderLine=" + orderLine + ", serialNo=" + serialNo + ", stealingTag="
				+ stealingTag + ", toCorrect=" + toCorrect + ", previousSerialNo=" + previousSerialNo
				+ ", previousStealingTag=" + previousStealingTag + ", creationDate=" + creationDate + ", orderLineId="
				+ orderLineId + "]";
	}
	public String getPreviousSerialNo() {
		return previousSerialNo;
	}
	public void setPreviousSerialNo(String previousSerialNo) {
		this.previousSerialNo = previousSerialNo;
	}
	public String getPreviousStealingTag() {
		return previousStealingTag;
	}
	public void setPreviousStealingTag(String previousStealingTag) {
		this.previousStealingTag = previousStealingTag;
	}
	public String getRegisteredBy() {
		return registeredBy;
	}
	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}
	@Transient
	public String getCreationDateDisplay() {		
		String result = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(creationDate);
		return result;		
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
	public String getCustomAttribute1Label() {
		return customAttribute1Label;
	}
	public void setCustomAttribute1Label(String customAttribute1Label) {
		this.customAttribute1Label = customAttribute1Label;
	}
	public String getCustomAttribute2Label() {
		return customAttribute2Label;
	}
	public void setCustomAttribute2Label(String customAttribute2Label) {
		this.customAttribute2Label = customAttribute2Label;
	}
	public String getCustomAttribute3Label() {
		return customAttribute3Label;
	}
	public void setCustomAttribute3Label(String customAttribute3Label) {
		this.customAttribute3Label = customAttribute3Label;
	}
	public String getCustomAttribute4Label() {
		return customAttribute4Label;
	}
	public void setCustomAttribute4Label(String customAttribute4Label) {
		this.customAttribute4Label = customAttribute4Label;
	}
	public String getCustomAttribute5Label() {
		return customAttribute5Label;
	}
	public void setCustomAttribute5Label(String customAttribute5Label) {
		this.customAttribute5Label = customAttribute5Label;
	}
	public String getCustomAttribute6Label() {
		return customAttribute6Label;
	}
	public void setCustomAttribute6Label(String customAttribute6Label) {
		this.customAttribute6Label = customAttribute6Label;
	}
	public String getCustomAttribute7Label() {
		return customAttribute7Label;
	}
	public void setCustomAttribute7Label(String customAttribute7Label) {
		this.customAttribute7Label = customAttribute7Label;
	}
	public String getCustomAttribute8Label() {
		return customAttribute8Label;
	}
	public void setCustomAttribute8Label(String customAttribute8Label) {
		this.customAttribute8Label = customAttribute8Label;
	}
}
