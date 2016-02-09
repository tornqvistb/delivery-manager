package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

import se.lanteam.domain.Equipment;

public class ReqOrderLine {

	List<Equipment> equipments = new ArrayList<Equipment>();

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}
	
	
}
