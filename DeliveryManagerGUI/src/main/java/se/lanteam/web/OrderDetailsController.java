package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.ListUtils;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.DeliveryArea;
import se.lanteam.domain.DeliveryPlan;
import se.lanteam.domain.DeliveryWeekDay;
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.OrderLine;
import se.lanteam.model.DeliveryDay;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;

@Controller
public class OrderDetailsController extends BaseController{

	private static final String STATUS_MSG_OK = "Statusmeddelande skickat.";
	private static final String STATUS_MSG_COMMENT_MISSING = "Du måste skriva ett meddelande.";
	private static final String STATUS_MSG_ORDER_CANCELLED = "Följande order har makulerats: ";
	private static final String STATUS_MSG_ORDER_FINISHED = "Följande order har klarmarkerats: ";
	private static final String STATUS_ROUTE_PLAN_OK = "Order har ruttplanerats.";
	private static final String STATUS_ROUTE_PLAN_NOK = "Ruttplanering av ordern misslyckades. Alla obligatoriska fält ej ifyllda.";

	private SessionBean sessionBean;

	@RequestMapping(value = "order-list/view/registerComment/{orderId}", method = RequestMethod.POST)
	public String registerMessage(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		if (StringUtils.hasText(reqAttr.getComment())) { 
			OrderComment comment = new OrderComment();
			comment.setOrderHeader(order);
			comment.setOrderLine(String.valueOf(reqAttr.getOrderLineId()));
			comment.setMessage(reqAttr.getComment());
			order.getOrderComments().add(comment);
			orderRepo.save(order);
			order = orderRepo.findOne(orderId);
			model.put("order", order);
			reqAttr = new RequestAttributes();
			reqAttr.setStatusMessageCreationSuccess(STATUS_MSG_OK);
			model.put("reqAttr", reqAttr);
		} else {
			model.put("order", order);
			reqAttr = new RequestAttributes();
			reqAttr.setStatusMessageCreationFailed(STATUS_MSG_COMMENT_MISSING);
			model.put("reqAttr", reqAttr);
		}
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		return "order-details";
	}

	@RequestMapping(value="order-list/correct/{orderId}", method=RequestMethod.GET)
	public String showOrderCorrectionView(@PathVariable Long orderId, ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		RequestAttributes reqAttr = new RequestAttributes(order);
		model.put("reqAttr", reqAttr);
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		return "correct-order";
	}
	
	@RequestMapping(value = "order-list/view/cancelOrder/{orderId}", method = RequestMethod.POST)
	public String cancelOrder(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		String orderNumber = order.getOrderNumber();
		orderRepo.delete(orderId);
		reqAttr = new RequestAttributes();
		reqAttr.setThanksMessage(STATUS_MSG_ORDER_CANCELLED + orderNumber);
		model.put("reqAttr", reqAttr);

		return "order-list";
	}

	@RequestMapping(value = "order-list/view/finishOrder/{orderId}", method = RequestMethod.POST)
	public String finishOrder(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		String orderNumber = order.getOrderNumber();
		if (StatusConstants.ORDER_STATUS_NOT_PICKED.equals(order.getStatus())
				|| StatusConstants.ORDER_STATUS_STARTED.equals(order.getStatus())) {
			order.setStatus(StatusConstants.ORDER_STATUS_REGISTRATION_DONE);
		} else if (StatusConstants.ORDER_STATUS_BOOKED.equals(order.getStatus())) {
			order.setStatus(StatusConstants.ORDER_STATUS_ROUTE_PLANNED);
		}
		for (OrderLine ol : order.getUnCompletedOrderLines()) {
			ol.setRested(true);
		}
		if (order.isChildOrderInJoint()) {
			String mainOrderNo = order.getJointDelivery();
			order.clearJointDelivery();
			orderRepo.save(order);
			List<OrderHeader> mainOrders = orderRepo.findOrdersByOrderNumber(mainOrderNo);
			if (!ListUtils.isEmpty(mainOrders)) {
				OrderHeader mainOrder = mainOrders.get(0);
				List<String> childOrders = mainOrder.getJoinedOrdersAsList();
				if (childOrders.size() == 1) {
					mainOrder.clearJointDelivery();
				} else if (childOrders.size() > 1) {
					String jdOrders = mainOrder.getJointDeliveryOrders().replace(order.getOrderNumber(), "");
					mainOrder.setJointDeliveryOrders(jdOrders);
					String jdOrderText = mainOrder.getJointDeliveryText().replace(order.getOrderNumber(), "");
					mainOrder.setJointDeliveryText(jdOrderText);
				}
				orderRepo.save(mainOrder);
			}
		} else if (order.isMainOrderInJoint()) {
			List<String> childOrders = order.getJoinedOrdersAsList();
			order.clearJointDelivery();
			orderRepo.save(order);
			for (String child : childOrders) {
				List<OrderHeader> childs = orderRepo.findOrdersByOrderNumber(child);
				if (childs.size() == 1) {
					OrderHeader childOrder = childs.get(0);
					childOrder.clearJointDelivery();
					orderRepo.save(childOrder);					
				}
			}
		} else {
			orderRepo.save(order);
		}
		reqAttr = new RequestAttributes();
		reqAttr.setThanksMessage(STATUS_MSG_ORDER_FINISHED + orderNumber);
		model.put("reqAttr", reqAttr);

		return "order-list";
	}
	
	@RequestMapping(value = "order-list/view/getdaybyarea/{areaId}", method = RequestMethod.POST)
	public @ResponseBody String getDaysByArea(@PathVariable Long areaId) {
		if (areaId == 0) {
			return "";
		}
		DeliveryArea area = deliveryAreaRepo.getOne(areaId);
		StringBuffer sb = new StringBuffer();
		List<DeliveryDay> deliveryDays = new ArrayList<DeliveryDay>();
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

		for (DeliveryDay deliveryDay : deliveryDays) {
			sb.append("<option value=\""+ DateUtil.dateToString(deliveryDay.getDate()) + "\">" + deliveryDay.getDayOfWeek() + " " + DateUtil.dateToString(deliveryDay.getDate()) + "</option>");
		}		
		return sb.toString();
	}

	@RequestMapping(value = "order-list/view/planroute/{orderId}", method = RequestMethod.POST)
	public String planRoute(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		Date deliveryDate = null;
		try {
			deliveryDate = DateUtil.stringToDate(reqAttr.getDeliveryDate());
		} catch (ParseException e) {
		}
		if (StringUtils.hasText(reqAttr.getDeliveryAreaId()) && deliveryDate != null) {
			planOrder(order, reqAttr, deliveryDate);
			for (OrderHeader relOrder : getRelatedOrders(order)) {
				planOrder(relOrder, reqAttr, deliveryDate);
			}
			order = orderRepo.findOne(orderId);
			model.put("order", order);
			reqAttr = new RequestAttributes();
			reqAttr = addRoutePlanning(reqAttr, order);
			reqAttr.setStatusRouteplanSuccess(STATUS_ROUTE_PLAN_OK);
		} else {
			model.put("order", order);
			reqAttr.setStatusRouteplanFailed(STATUS_ROUTE_PLAN_NOK);			
		}
		reqAttr = addRelatedOrders(reqAttr, order);
		model.put("reqAttr", reqAttr);
		model.put("deliveryAreas", deliveryAreaRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		return "order-details";
	}
	private void planOrder(OrderHeader order, RequestAttributes reqAttr, Date planDate) {
		DeliveryPlan deliveryPlan = new DeliveryPlan();
		deliveryPlan.setComment(reqAttr.getComment());
		deliveryPlan.setDeliveryArea(deliveryAreaRepo.getOne(Long.parseLong(reqAttr.getDeliveryAreaId())));
		deliveryPlan.setOrderHeader(order);
		deliveryPlan.setPlannedDeliveryDate(planDate);
		order.setDeliveryPlan(deliveryPlan);
		if (order.getCustomerGroup().getBookOrderBeforeRegistration()) {
			if (order.getUnCompletedOrderLines().size() == 0) {
				order.setStatus(StatusConstants.ORDER_STATUS_ROUTE_PLANNED);
			} else { 
				order.setStatus(StatusConstants.ORDER_STATUS_BOOKED);
			}
		} else {
			order.setStatus(StatusConstants.ORDER_STATUS_ROUTE_PLANNED);
		}
		orderRepo.save(order);
	}
	@RequestMapping(value = "order-list/new-routeplan/{orderId}", method = RequestMethod.GET)
	public String newRoutePlan(@PathVariable Long orderId, ModelMap model) {		
		OrderHeader order = orderRepo.findOne(orderId);
		unPlanOrder(order);
		for (OrderHeader relOrder : getRelatedOrders(order)) {
			unPlanOrder(relOrder);
		}
		return "redirect:/order-list/view/{orderId}";
	}

	@RequestMapping(value = "order-list/generate-rows/{orderId}", method = RequestMethod.POST)
	public String generateRows(@PathVariable Long orderId) {		
		OrderHeader order = orderRepo.findOne(orderId);
		order.setOrderLines(null);
		Set<OrderLine> orderLines = new HashSet<>();
		for (int i = 1; i < 1001; i++) {
			OrderLine line = new OrderLine();
			line.setTotal(1);
			line.setRemaining(1);
			line.setRegistered(0);
			List<String> serialNumbers = new ArrayList<>();
			serialNumbers.add("SERIAL_" + i);
			line.addPickedSerialNumbers(serialNumbers);
			line.setArticleNumber("TEST_ARTICLE");
			line.setArticleDescription("Desc testarticle");
			line.setRowNumber(i);
			line.setAutoRegistered(false);
			line.setCustomerRowNumber(i);
			line.setOrderHeader(order);
			line.setRestrictionCode("1");
			orderLines.add(line);
		}
		order.setOrderLines(orderLines);
		orderRepo.save(order);
		return "redirect:/order-list/view/{orderId}";
	}

	@RequestMapping(value = "order-list/registration-method/{orderId}", method = RequestMethod.POST)
	public String changeRegistrationMethod(@ModelAttribute OrderHeader orderAttr, @PathVariable Long orderId,
			ModelMap model) {
		return updateRegistrationMethod(orderId, orderAttr.getRegistrationMethod());
	}	
	
	private String updateRegistrationMethod(Long orderId, int newValue) {
		OrderHeader order = orderRepo.findOne(orderId);
		order.setRegistrationMethod(newValue);
		orderRepo.save(order);
		return "redirect:/order-list/view/{orderId}";		
	}
	
	private void unPlanOrder(OrderHeader order) {
		if (order.getCustomerGroup().getBookOrderBeforeRegistration()) {
			if (order.getUnCompletedOrderLines().size() == 0) {
				order.setStatus(StatusConstants.ORDER_STATUS_REGISTRATION_DONE);
			} else {
				if (order.getPickStatus() == StatusConstants.PICK_STATUS_NOT_PICKED) {
					order.setStatus(StatusConstants.ORDER_STATUS_NOT_PICKED);
				} else {
					order.setStatus(StatusConstants.ORDER_STATUS_STARTED);
				}				
			}
		} else {
			order.setStatus(StatusConstants.ORDER_STATUS_REGISTRATION_DONE);
		}
		order.setDeliveryPlan(null);
		orderRepo.save(order);
	}

	@RequestMapping(value = "order-list/send-delivery-info/{orderId}", method = RequestMethod.GET)
	public String sendDeliveryInfo(@PathVariable Long orderId, ModelMap model) {		
		OrderHeader order = orderRepo.findOne(orderId);
		setOrderForCustomerDelivery(order);
		// Kolla med Magnus om samlevererade ordrar också skall uppdateras
		for (OrderHeader relOrder : getRelatedOrders(order)) {
			setOrderForCustomerDelivery(relOrder);
		}
		return "redirect:/order-list/view/{orderId}";
	}
	
	private void setOrderForCustomerDelivery(OrderHeader order) {
		order.setStatus(StatusConstants.ORDER_STATUS_SENT_CUSTOMER);
		orderRepo.save(order);
	}	
	
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
