package se.lanteam.web;

import java.util.Date;
import java.util.Iterator;

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
import se.lanteam.repository.OrderLineRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class OrderDetailsController {

	private OrderRepository orderRepo;
	private OrderLineRepository orderLineRepo;

	@RequestMapping(value = "order-list/view/registerEquipment/{orderId}", method = RequestMethod.POST)
	public String registerEquipment(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {

		OrderLine orderLine = orderLineRepo.findOne(reqAttr.getOrderLineId());
		Equipment equipment = new Equipment();
		equipment.setCreationDate(new Date());
		equipment.setOrderLine(orderLine);
		equipment.setSerialNo(reqAttr.getSerialNo());
		equipment.setStealingTag(reqAttr.getStealingTag());
		orderLine.getEquipments().add(equipment);
		orderLine.setRegistered(orderLine.getRegistered() + 1);
		orderLine.setRemaining(orderLine.getRemaining() - 1);
		orderLineRepo.save(orderLine);
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		long orderLineId = reqAttr.getOrderLineId();
		reqAttr = new RequestAttributes();
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

	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@Autowired
	public void setOrderLineRepo(OrderLineRepository orderLineRepo) {
		this.orderLineRepo = orderLineRepo;
	}


}
