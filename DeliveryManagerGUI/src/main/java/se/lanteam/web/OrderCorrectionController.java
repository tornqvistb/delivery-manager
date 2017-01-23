	package se.lanteam.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.Equipment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.EquipmentRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class OrderCorrectionController {

	private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);

	private OrderRepository orderRepo;	
	private EquipmentRepository equipmentRepo;	

	@RequestMapping(value="order-list/correct/doCorrect/{orderId}/{equipmentId}", method=RequestMethod.GET)
	public String doCorrectEquipment(@PathVariable Long orderId, @PathVariable Long equipmentId, ModelMap model) {
		LOG.info("In doCorrectEquipment");
		Equipment equipment = equipmentRepo.findOne(equipmentId);
		equipment.setToCorrect(true);
		equipmentRepo.save(equipment);
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		RequestAttributes reqAttr = new RequestAttributes(order);
		model.put("reqAttr", reqAttr);
		return "correct-order";
	}
	@RequestMapping(value="order-list/correct/undoCorrect/{orderId}/{equipmentId}", method=RequestMethod.GET)
	public String undoCorrectEquipment(@PathVariable Long orderId, @PathVariable Long equipmentId, ModelMap model) {
		LOG.info("In undoCorrectEquipment");
		Equipment equipment = equipmentRepo.findOne(equipmentId);
		equipment.setToCorrect(false);
		equipmentRepo.save(equipment);
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		RequestAttributes reqAttr = new RequestAttributes(order);
		model.put("reqAttr", reqAttr);
		return "correct-order";
	}
	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setEquipmentRepo(EquipmentRepository equipmentRepo) {
		this.equipmentRepo = equipmentRepo;
	}
}
