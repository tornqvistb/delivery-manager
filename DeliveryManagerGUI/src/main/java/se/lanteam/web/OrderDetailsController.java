package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.DeliveryArea;
import se.lanteam.domain.DeliveryPlan;
import se.lanteam.domain.DeliveryWeekDay;
import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.DeliveryDay;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.DeliveryAreaRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class OrderDetailsController extends BaseController{

	private static final String STATUS_MSG_OK = "Statusmeddelande skickat.";
	private static final String STATUS_MSG_COMMENT_MISSING = "Du måste skriva ett meddelande.";
	private static final String STATUS_MSG_ORDER_CANCELLED = "Följande order har makulerats: ";
	private static final String STATUS_ROUTE_PLAN_OK = "Order har ruttplanerats.";
	private static final String STATUS_ROUTE_PLAN_NOK = "Ruttplanering av ordern misslyckades. Alla obligatoriska fält ej ifyllda.";

	private SessionBean sessionBean;
	private DeliveryAreaRepository deliveryAreaRepo;


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
		order.setStatus(StatusConstants.ORDER_STATUS_ROUTE_PLANNED);			
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

	private void unPlanOrder(OrderHeader order) {
		order.setStatus(StatusConstants.ORDER_STATUS_REGISTRATION_DONE);
		order.setDeliveryPlan(null);
		orderRepo.save(order);
	}
	
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	@Autowired
	public void setDeliveryAreaRepo(DeliveryAreaRepository deliveryAreaRepo) {
		this.deliveryAreaRepo = deliveryAreaRepo;
	}

}
