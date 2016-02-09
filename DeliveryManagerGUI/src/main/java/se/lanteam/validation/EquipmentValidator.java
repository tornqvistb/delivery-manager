package se.lanteam.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.EquipmentRepository;

@Service
public class EquipmentValidator {

	private static String RESULT_OK = "";
	private static String SERIAL_NO_TOO_SHORT = "Serienummer måste vara minst 7 tecken långt";
	private static String INVALID_STEALING_TAG = "Stöld-ID måste vara 6 tecken";
	private static String SERIAL_NO_ON_CURRENT_ORDER = "Angivet serienummer finns redan registrerat på denna order";
	private static String STEALING_TAG_ON_CURRENT_ORDER = "Angivet stöld-ID finns redan registrerat på denna order";
	private static String SERIAL_NO_ON_OTHER_ORDER = "Angivet serienummer finns redan registrerat på order ";
	private static String STEALING_TAG_ON_OTHER_ORDER = "Angivet stöld-ID finns redan registrerat på order ";

	
	private EquipmentRepository equipmentRepo;

	public String validateEquipmentOnCorrection(Equipment equipment, OrderHeader order) {
		
		String result = RESULT_OK;
		
		if (equipment.getSerialNo() != null && equipment.getSerialNo().equals(equipment.getPreviousSerialNo())) {
			// No change of SerNo
		} else {
			result = validateSerialNo(equipment, order);
		}
		
		if (RESULT_OK.equals(result)) {
			if (equipment.getStealingTag() != null && equipment.getStealingTag().equals(equipment.getPreviousStealingTag())) {
				// No change of Stealtag
			} else {
				result = validateStealingTag(equipment, order);
			}
		}
				
		return result;
	}

	
	public String validateEquipment(Equipment equipment, OrderHeader order) {
		String result = validateSerialNo(equipment, order);
		
		if (RESULT_OK.equals(result)) {
			result = validateStealingTag(equipment, order);
		}
				
		return result;
	}

	private String validateSerialNo(Equipment equipment, OrderHeader order) {
		// - serial number minimum 7 letters
		if (equipment.getSerialNo() == null || equipment.getSerialNo().length() < 7) {
			return SERIAL_NO_TOO_SHORT;
		}
		// - serial number not registered on current order
		for (OrderLine line : order.getOrderLines()) {
			for (Equipment equip : line.getEquipments()) {
				if (equip.getSerialNo().equals(equipment.getSerialNo())) {
					return SERIAL_NO_ON_CURRENT_ORDER + " (" + equipment.getSerialNo() + ")";
				}
			}
		}
		// - serial number not registered on other order
		List<Equipment> equipments = equipmentRepo.findBySerialNo(equipment.getSerialNo());
		if (equipments != null && equipments.size() > 0) {
			Equipment equip = equipments.get(0);
			if (equip.getOrderLine() != null && equip.getOrderLine().getOrderHeader() != null) {
				return SERIAL_NO_ON_OTHER_ORDER + equip.getOrderLine().getOrderHeader().getOrderNumber() + " (" + equipment.getSerialNo() + ")";
			}
		}
		
		return RESULT_OK;
	}

	private String validateStealingTag(Equipment equipment, OrderHeader order) {
		// - stealing tag exact 6 letters
		if (equipment.getStealingTag() == null || equipment.getStealingTag().length() != 6) {
			return INVALID_STEALING_TAG;
		}		
		// - stealing tag not registered on current order
		for (OrderLine line : order.getOrderLines()) {
			for (Equipment equip : line.getEquipments()) {
				if (equip.getStealingTag().equals(equipment.getStealingTag())) {
					return STEALING_TAG_ON_CURRENT_ORDER + " (" + equipment.getStealingTag() + ")";
				}
			}
		}		
		// - stealing tag not registered on other order
		List<Equipment> equipments = equipmentRepo.findByStealingTag(equipment.getStealingTag());
		if (equipments != null && equipments.size() > 0) {
			Equipment equip = equipments.get(0);
			if (equip.getOrderLine() != null && equip.getOrderLine().getOrderHeader() != null) {
				return STEALING_TAG_ON_OTHER_ORDER + equip.getOrderLine().getOrderHeader().getOrderNumber() + " (" + equipment.getStealingTag() + ")";
			}
		}
		
		return RESULT_OK;
	}
	
	@Autowired
	public void setEquipmentRepo(EquipmentRepository equipmentRepo) {
		this.equipmentRepo = equipmentRepo;
	}
	
}
