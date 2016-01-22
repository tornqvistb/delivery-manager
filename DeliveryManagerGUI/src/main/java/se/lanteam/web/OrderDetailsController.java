package se.lanteam.web;

import java.util.Date;

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

	@RequestMapping(value="registerEquipment/{orderId}", method=RequestMethod.POST)
	public String registerEquipment(@ModelAttribute Equipment equipment, 
				@PathVariable Long orderId, 
				ModelMap model)
	{
		
		OrderLine orderLine = orderLineRepo.findOne(equipment.getOrderLineId());
		equipment.setCreationDate(new Date());
		equipment.setOrderLine(orderLine);
		orderLine.getEquipments().add(equipment);
		orderLineRepo.save(orderLine);
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		model.put("equipment", new Equipment());
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
