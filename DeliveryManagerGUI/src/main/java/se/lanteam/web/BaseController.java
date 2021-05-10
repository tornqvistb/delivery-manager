package se.lanteam.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import se.lanteam.constants.DateUtil;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.DeliveryArea;
import se.lanteam.domain.DeliveryWeekDay;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.DeliveryDay;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SearchBean;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.DeliveryAreaRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class BaseController {
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	        binder.setAutoGrowCollectionLimit(2048);
	}
	
	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	protected OrderRepository orderRepo;
	protected CustomerGroupRepository customerRepo;
	protected SearchBean searchBean;
	protected DeliveryAreaRepository deliveryAreaRepo;
	
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
		List<OrderHeader> relatedOrders = new ArrayList<>();
		for (String orderNumber : orderNumbers) {
			List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderNumber);
			if (!orders.isEmpty()) {
				relatedOrders.add(orders.get(0));
			}
		}
		return relatedOrders;
	}

	private List<String> getRelatedOrderNumbers(OrderHeader order) {
		List<String> otherOrdersInDelivery = new ArrayList<>();
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

	protected List<CustomerCustomField> getCustomerCustomFields(String report) {
		List<CustomerCustomField> result = new ArrayList<>();
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

	protected RequestAttributes addRoutePlanning(RequestAttributes reqAttr, OrderHeader order) {
		if (order.getDeliveryPlan() != null) {
			reqAttr.setDeliveryAreaId(String.valueOf(order.getDeliveryPlan().getDeliveryArea().getId()));
			reqAttr.setDeliveryDayId(DateUtil.dateToString(order.getDeliveryPlan().getPlannedDeliveryDate()));
			reqAttr.setDeliveryDays(getDeliveryDaysForArea(order.getDeliveryPlan().getDeliveryArea().getId()));
			reqAttr.setDeliveryDate(DateUtil.dateToString(order.getDeliveryPlan().getPlannedDeliveryDate()));
			reqAttr.setComment(order.getDeliveryPlan().getComment());
		}
		return reqAttr;
	}
	
	protected List<DeliveryDay> getDeliveryDaysForArea(Long areaId) {
	
		DeliveryArea area = deliveryAreaRepo.getOne(areaId);
		List<DeliveryDay> deliveryDays = new ArrayList<>();
		for (DeliveryWeekDay day : area.getDeliveryWeekDays()) {
			Date lastDate = new Date();
			for (int i = 0; i < 10; i++) {
				Date nextDate = DateUtil.getNextDateByWeekday(lastDate, day.getDayOfWeek());
				deliveryDays.add(new DeliveryDay(nextDate, day.getName()));
				lastDate = DateUtil.addDaysToDate(nextDate, 1);
			}
		}
		// sort
		Collections.sort(deliveryDays, new Comparator<DeliveryDay>() {
			@Override
			public int compare(DeliveryDay d1, DeliveryDay d2) {
				String date1 = DateUtil.dateToString(d1.getDate());
				String date2 = DateUtil.dateToString(d2.getDate());
				return date1.compareTo(date2);
			}
		});
		return deliveryDays;
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

	@Autowired
	public void setDeliveryAreaRepo(DeliveryAreaRepository deliveryAreaRepo) {
		this.deliveryAreaRepo = deliveryAreaRepo;
	}

	
}
