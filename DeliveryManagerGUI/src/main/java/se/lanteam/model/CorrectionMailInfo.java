package se.lanteam.model;

import java.util.List;

import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;

public class CorrectionMailInfo {

	private OrderHeader orderHeader;
	private List<Equipment> modifiedEquipment;
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
