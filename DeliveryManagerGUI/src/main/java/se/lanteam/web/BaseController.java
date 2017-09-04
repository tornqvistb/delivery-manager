package se.lanteam.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.OrderRepository;

@Controller
public class BaseController {
	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	protected OrderRepository orderRepo;

	protected RequestAttributes addRelatedOrders(RequestAttributes reqAttr, OrderHeader order) {
		if (order.isPartOfJointdelivery()) {
			reqAttr.setRelatedOrders(getRelatedOrders(order));
		}
		return reqAttr;
	}
	
	protected List<OrderHeader> getRelatedOrders(OrderHeader order) {
		List<String> orderNumbers = getRelatedOrderNumbers(order);
		List<OrderHeader> relatedOrders = new ArrayList<OrderHeader>();
		for (String orderNumber : orderNumbers) {
			List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderNumber);
			if (!orders.isEmpty()) {
				relatedOrders.add(orders.get(0));
			}
		}
		return relatedOrders;
	}

	private List<String> getRelatedOrderNumbers(OrderHeader order) {
		List<String> otherOrdersInDelivery = new ArrayList<String>();
		if (order.isPartOfJointdelivery()) {
			if (order.isMainOrderInJoint()) {
				otherOrdersInDelivery = Arrays.asList(order.getJointDeliveryOrders().split(","));
			} else {
				// child order
				otherOrdersInDelivery.add(order.getJointDeliveryOrders());
				List<OrderHeader> masterOrder = orderRepo.findOrdersByOrderNumber(order.getJointDeliveryOrders());
				if (masterOrder.size() > 0) {
					List<String> allChildren = Arrays.asList(masterOrder.get(0).getJointDeliveryOrders().split(","));
					for (String child : allChildren) {
						if (!order.getOrderNumber().equals(child)) {
							otherOrdersInDelivery.add(child);
						}
					}
				}
			}
		}
		return otherOrdersInDelivery;
	}

	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	
}
