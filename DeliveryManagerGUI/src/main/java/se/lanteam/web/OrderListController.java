package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.SessionConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.constants.StatusUtil;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.DeliveryAreaRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PropertyService;

@Controller
public class OrderListController {
	
	private OrderRepository orderRepo;
	private PropertyService propService;
	private SessionBean sessionBean;
	private DeliveryAreaRepository deliveryAreaRepo;

	private String checkCustomerGroup(HttpServletRequest request) {
		String result = null;
		CustomerGroup custGroup = (CustomerGroup) request.getSession().getAttribute(SessionConstants.CURRENT_CUSTOMER_GROUP);
		if (custGroup == null) {
			result = "redirect:choose-customer-group";
		}
		return result;
	}
	
	@RequestMapping("/")
	public String showStartPage(HttpServletRequest request) {
		String customerRedirectLink = checkCustomerGroup(request);
		if (customerRedirectLink != null) 
			return customerRedirectLink;
		return "redirect:order-list";
	}
		
	@RequestMapping("order-list")
	public String showOrderList(ModelMap model, HttpServletRequest request) {
		String customerRedirectLink = checkCustomerGroup(request);
		if (customerRedirectLink != null) 
			return customerRedirectLink;
		List<OrderHeader> orders = orderRepo.findOrdersByStatusList(Arrays.asList(StatusConstants.ACTIVE_STATI), sessionBean.getCustomerGroup().getId());
		model.put("orders", orders);
		model.put("reqAttr", new RequestAttributes());
		model.put("color", "red");
		return "order-list";
	}
	@RequestMapping(value="order-list/view/{orderId}", method=RequestMethod.GET)
	public String showOrderView(@PathVariable Long orderId, ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("reqAttr", new RequestAttributes());
		model.put("order", order);
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		model.put("deliveryAreas", deliveryAreaRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
		return "order-details";
	}
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setPropertyService(PropertyService propService) {
		this.propService = propService;
	}
	@Autowired
	public void setDeliveryAreaRepo(DeliveryAreaRepository deliveryAreaRepo) {
		this.deliveryAreaRepo = deliveryAreaRepo;
	}

	@RequestMapping(value="order-list/search", method=RequestMethod.GET)
	public String searchOrders(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		String status = reqAttr.getOrderStatus();
		
		String query = "%" + reqAttr.getQuery() + "%";
		
		try {
			Date fromDate = DateUtil.getDefaultStartDate();
			if (!StringUtils.isEmpty(reqAttr.getFromDate())) {
				fromDate = DateUtil.stringToDate(reqAttr.getFromDate());
			}
			
			Date toDate = DateUtil.getTomorrow();
			if (!StringUtils.isEmpty(reqAttr.getToDate())) {
				toDate = DateUtil.stringToDate(reqAttr.getToDate());
			}
			
			Date orderDate = DateUtil.getDefaultStartDate();
			List<OrderHeader> orders;
			List<String> stati = new ArrayList<String>();
			if (status.equals(StatusConstants.ORDER_STATUS_GROUP_ACTIVE)){
				stati = Arrays.asList(StatusConstants.ACTIVE_STATI);
			} else if (status.equals(StatusConstants.ORDER_STATUS_GROUP_INACTIVE)) {
				stati = Arrays.asList(StatusConstants.INACTIVE_STATI);
				orderDate = DateUtil.getStartDateForInactiveOrders(propService.getLong(PropertyConstants.MAX_DAYS_INACTIVE_ORDERS_SEARCH).intValue());
				reqAttr.setInfoMessage("De ordrar som visas är skapta " + propService.getLong(PropertyConstants.MAX_DAYS_INACTIVE_ORDERS_SEARCH).intValue() + " dagar tillbaks i tiden fram tills idag. För att se äldre inaktiva ordrar, sök på specifik status.");
			} else {
				stati.add(status);
			}
			if (StatusUtil.isActiveStatus(status)) {
				orders = orderRepo.findOrdersFromSearch(stati, orderDate, query, sessionBean.getCustomerGroup().getId());
			} else {
				orders = orderRepo.findDeliveredOrdersFromSearch(stati, orderDate, query, fromDate, toDate, sessionBean.getCustomerGroup().getId());
			}
			model.put("orders", orders);
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}		
		reqAttr.setOrderStatus(status);
		model.put("reqAttr", reqAttr);
		return "order-list";
	}
	
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
}
