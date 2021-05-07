package se.lanteam.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import se.lanteam.constants.RestrictionCodes;
import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.EquipmentRepository;

@Service
public class EquipmentValidator {

	private static final String RESULT_OK = "";
	private static final String STEALING_TAG_EMPTY = "Stöld-ID får ej vara tomt";
	private static final String REGISTERED_BY_MISSING = "Du måste fylla i Registrerad av";
	private static final String STEALING_TAG_ON_CURRENT_ORDER = "Angivet stöld-ID finns redan registrerat på denna order";
	private static final String STEALING_TAG_ON_OTHER_ORDER = "Angivet stöld-ID finns redan registrerat på order ";
	private static final String CUSTOM_FIELD_MANDATORY = "Du måste fylla i ";
	
	private EquipmentRepository equipmentRepo;
	private SessionBean sessionBean;


	public String validateEquipmentOnCorrection(Equipment equipment, OrderHeader order) {
		
		String result = RESULT_OK;
		
		if (equipment.getStealingTag() != null && equipment.getStealingTag().equals(equipment.getPreviousStealingTag())) {
			// No change of Stealtag
		} else {
			result = validateStealingTag(equipment, order);
		}
		
		if (RESULT_OK.equals(result)) {
			result = validateRegisteredBy(equipment);
		}
		return result;
	}
	
	
	public String validateEquipment(Equipment equipment, OrderHeader order, String restrictionCode, boolean isUpdate) {
				
		String result = RESULT_OK;
		
		RegistrationConfig regConfig = sessionBean.getCustomerGroup().getRegistrationConfig();
		if (RESULT_OK.equals(result) && !RestrictionCodes.NO_SLA_SERIALN0_NO_STEALING_TAG.equals(restrictionCode)) {
			result = validateStealingTag(equipment, order);
		}
		
		if (RESULT_OK.equals(result)) {
			result = validateRegisteredBy(equipment);
		}		
		
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute1() && regConfig.getMandatoryAttribute1() && StringUtils.isEmpty(equipment.getCustomAttribute1())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute1(); 
		}
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute2() && regConfig.getMandatoryAttribute2() && StringUtils.isEmpty(equipment.getCustomAttribute2())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute2(); 
		}
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute3() && regConfig.getMandatoryAttribute3() && StringUtils.isEmpty(equipment.getCustomAttribute3())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute3(); 
		}
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute4() && regConfig.getMandatoryAttribute4() && StringUtils.isEmpty(equipment.getCustomAttribute4())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute4(); 
		}
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute5() && regConfig.getMandatoryAttribute5() && StringUtils.isEmpty(equipment.getCustomAttribute5())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute5(); 
		}
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute6() && regConfig.getMandatoryAttribute6() && StringUtils.isEmpty(equipment.getCustomAttribute6())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute6(); 
		}
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute7() && regConfig.getMandatoryAttribute7() && StringUtils.isEmpty(equipment.getCustomAttribute7())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute7(); 
		}
		if (RESULT_OK.equals(result) && regConfig.getUseAttribute8() && regConfig.getMandatoryAttribute8() && StringUtils.isEmpty(equipment.getCustomAttribute8())) {
			result = CUSTOM_FIELD_MANDATORY + regConfig.getLabelAttribute8(); 
		}
		return result;
	}
	private String validateStealingTag(Equipment equipment, OrderHeader order) {
		// - stealing not empty
		if (equipment.getStealingTag() == null || StringUtils.isEmpty(equipment.getStealingTag())) {
			return STEALING_TAG_EMPTY;
		}

		// - stealing tag not registered on current order
		for (OrderLine line : order.getOrderLines()) {
			for (Equipment equip : line.getEquipments()) {
				if (equip.getId() != equipment.getId() && equip.getStealingTag() != null && equip.getStealingTag().equals(equipment.getStealingTag())) {
					return STEALING_TAG_ON_CURRENT_ORDER + " (" + equipment.getStealingTag() + ")";
				}
			}
		}		
		// - stealing tag not registered on other order
		List<Equipment> equipments = equipmentRepo.findByStealingTag(equipment.getStealingTag());
		if (equipments != null && equipments.size() > 0) {
			Equipment equip = equipments.get(0);
			if (equip.getOrderLine() != null && equip.getOrderLine().getOrderHeader() != null && equip.getOrderLine().getOrderHeader().getId() != order.getId()) {
				return STEALING_TAG_ON_OTHER_ORDER + equip.getOrderLine().getOrderHeader().getOrderNumber() + " (" + equipment.getStealingTag() + ")";
			}
		}
		
		return RESULT_OK;
	}
	
	private String validateRegisteredBy(Equipment equipment) {
		if (StringUtils.isEmpty(equipment.getRegisteredBy())) {
			return REGISTERED_BY_MISSING;
		}		
		return RESULT_OK;
	}
	
	@Autowired
	public void setEquipmentRepo(EquipmentRepository equipmentRepo) {
		this.equipmentRepo = equipmentRepo;
	}
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
}
