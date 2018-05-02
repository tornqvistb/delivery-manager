package se.lanteam.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SearchBean;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class BaseController {
	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	protected OrderRepository orderRepo;
	protected CustomerGroupRepository customerRepo;
	protected SearchBean searchBean;
	
	protected static final String SLA_REPORT = "SLA_REPORT";
	protected static final String DELIVERY_REPORT = "DELIVERY_REPORT";

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
				List<OrderHeader> childOrders = orderRepo.findOrdersByJointDelivery(order.getNetsetOrderNumber());
				if (!childOrders.isEmpty()) {
					for (OrderHeader oh : childOrders) {
						otherOrdersInDelivery.add(oh.getOrderNumber());
					}
				}
			} else {
				// child order
				List<OrderHeader> orders = orderRepo.findOrdersByNetsetOrderNumber(order.getJointDelivery());
				OrderHeader mainOrder = null;
				if (!orders.isEmpty()) {
					mainOrder = orders.get(0);
					otherOrdersInDelivery.add(mainOrder.getOrderNumber());
					List<OrderHeader> childOrders = orderRepo.findOrdersByJointDelivery(mainOrder.getNetsetOrderNumber());
					if (!childOrders.isEmpty()) {
						for (OrderHeader oh : childOrders) {
							if (!oh.getOrderNumber().equals(order.getOrderNumber())) {
								otherOrdersInDelivery.add(oh.getOrderNumber());
							}
						}
					}
				}				
			}
		}
		return otherOrdersInDelivery;
	}

/*
	private List<String> getRelatedOrderNumbers(OrderHeader order) {
		List<String> otherOrdersInDelivery = new ArrayList<String>();
		if (order.isPartOfJointdelivery()) {
			if (order.isMainOrderInJoint()) {
				if (!StringUtils.isEmpty(order.getJointDeliveryOrders())) {
					otherOrdersInDelivery = Arrays.asList(order.getJointDeliveryOrders().split(","));
				}
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
*/	
	protected List<CustomerCustomField> getCustomerCustomFields(String report) {
		List<CustomerCustomField> result = new ArrayList<CustomerCustomField>();
		if (searchBean.getCustomerGroupId() > 0) {
			CustomerGroup custGroup = customerRepo.findOne(searchBean.getCustomerGroupId());
			for (CustomerCustomField field : custGroup.getCustomerCustomFields()) {
				if (SLA_REPORT.equals(report) && field.getShowInSlaReport()) {
					result.add(field);
				}
				if (DELIVERY_REPORT.equals(report) && field.getShowInDeliveryReport()) {
					result.add(field);
				}
			}
		}
		return result;
	}
	
	protected String getOrderCustomFieldValue(CustomerCustomField customField, OrderHeader order) {
		String value = "";
		for (OrderCustomField orderCustomField : order.getOrderCustomFields()) {
			if (orderCustomField.getCustomField().getIdentification() == customField.getCustomField().getIdentification()) {
				value = orderCustomField.getValue();
				break;
			}
		}
		return value;
	}

	
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	@Autowired
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

}
