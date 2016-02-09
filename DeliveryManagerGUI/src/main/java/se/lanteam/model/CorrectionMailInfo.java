package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;

public class CorrectionMailInfo {
	
	public CorrectionMailInfo(OrderHeader orderHeader) {
		super();
		this.orderHeader = orderHeader;
	}
	private OrderHeader orderHeader;
	private List<Equipment> modifiedEquipment = new ArrayList<Equipment>();
	public OrderHeader getOrderHeader() {
		return orderHeader;
	}
	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}
	public List<Equipment> getModifiedEquipment() {
		return modifiedEquipment;
	}
	public void setModifiedEquipment(List<Equipment> modifiedEquipment) {
		this.modifiedEquipment = modifiedEquipment;
	}
	
}
