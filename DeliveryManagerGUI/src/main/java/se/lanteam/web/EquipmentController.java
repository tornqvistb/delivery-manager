package se.lanteam.web;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.repository.EquipmentRepository;
import se.lanteam.repository.OrderLineRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class EquipmentController {

	private static String RESULT_OK = "";
	private static String SERIAL_NO_TOO_SHORT = "Serienummer måste vara minst 7 tecken långt";
	private static String INVALID_STEALING_TAG = "Stöld-ID måste vara 6 tecken";
	private static String SERIAL_NO_ON_CURRENT_ORDER = "Angivet serienummer finns redan registrerat på denna order";
	private static String STEALING_TAG_ON_CURRENT_ORDER = "Angivet stöld-ID finns redan registrerat på denna order";
	private static String SERIAL_NO_ON_OTHER_ORDER = "Angivet serienummer finns redan registrerat på order ";
	private static String STEALING_TAG_ON_OTHER_ORDER = "Angivet stöld-ID finns redan registrerat på order ";


	private OrderRepository orderRepo;
	private OrderLineRepository orderLineRepo;
	private EquipmentRepository equipmentRepo;
	
	@RequestMapping(value = "order-list/view/registerEquipment/{orderId}", method = RequestMethod.POST)
	public String registerEquipment(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {

		OrderLine orderLine = orderLineRepo.findOne(reqAttr.getOrderLineId());
		Equipment equipment = new Equipment();
		equipment.setCreationDate(new Date());
		equipment.setOrderLine(orderLine);
		equipment.setSerialNo(reqAttr.getSerialNo());
		equipment.setStealingTag(reqAttr.getStealingTag());
		String valResult = validateEquipment(equipment, orderRepo.findOne(orderId));
		if (valResult.equals(RESULT_OK)) {
			orderLine.getEquipments().add(equipment);
			orderLine.setRegistered(orderLine.getRegistered() + 1);
			orderLine.setRemaining(orderLine.getRemaining() - 1);
			orderLineRepo.save(orderLine);
		} 
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		long orderLineId = reqAttr.getOrderLineId();
		reqAttr = new RequestAttributes();
		reqAttr.setRegEquipmentResult(valResult);
		reqAttr.setOrderLineId(orderLineId);
		model.put("reqAttr", reqAttr);
		return "order-details";
	}


	@RequestMapping(value = "order-list/view/deleq/{orderId}/{orderLineId}/{equipmentId}", method = RequestMethod.GET)
	public String deleteEquipment(@PathVariable Long orderId, @PathVariable Long orderLineId,
			@PathVariable Long equipmentId, ModelMap model) {
		System.out.println("equipmentId: " + equipmentId + " ,orderId: " + orderId);
		OrderLine orderLine = orderLineRepo.findOne(orderLineId);
		for (Iterator<Equipment> iterator = orderLine.getEquipments().iterator(); iterator.hasNext();) {
			Equipment equipment = iterator.next();
			if (equipment.getId() == equipmentId) {
				equipment.setOrderLine(null);
				iterator.remove();
				orderLine.setRegistered(orderLine.getRegistered() - 1);
				orderLine.setRemaining(orderLine.getRemaining() + 1);
			}
		}
		orderLineRepo.save(orderLine);
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		model.put("reqAttr", new RequestAttributes());
		return "order-details";
	}

	public String validateEquipment(Equipment equipment, OrderHeader order) {
		// To check:
		// - serial number minimum 7 letters
		if (equipment.getSerialNo() == null || equipment.getSerialNo().length() < 7) {
			return SERIAL_NO_TOO_SHORT;
		}
		// - stealing tag exact 6 letters
		if (equipment.getStealingTag() == null || equipment.getStealingTag().length() != 6) {
			return INVALID_STEALING_TAG;
		}		
		// - serial number or stealing tag not registered on current order
		for (OrderLine line : order.getOrderLines()) {
			for (Equipment equip : line.getEquipments()) {
				if (equip.getSerialNo().equals(equipment.getSerialNo())) {
					return SERIAL_NO_ON_CURRENT_ORDER + " (" + equipment.getSerialNo() + ")";
				} else if (equip.getStealingTag().equals(equipment.getStealingTag())) {
					return STEALING_TAG_ON_CURRENT_ORDER + " (" + equipment.getStealingTag() + ")";
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
		
		// - stealing tag not registered on other order
		equipments = equipmentRepo.findByStealingTag(equipment.getStealingTag());
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
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@Autowired
	public void setOrderLineRepo(OrderLineRepository orderLineRepo) {
		this.orderLineRepo = orderLineRepo;
	}
}
