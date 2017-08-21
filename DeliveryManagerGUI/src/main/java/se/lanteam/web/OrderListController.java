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
import se.lanteam.constants.StatusConstants;
import se.lanteam.constants.StatusUtil;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.OrderListSearchBean;
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
	private OrderListSearchBean orderListSearchBean;

	
	@RequestMapping("/")
	public String showStartPage(HttpServletRequest request) {
		return "redirect:order-list";
	}
		
	@RequestMapping("order-list")
	public String showOrderList(ModelMap model, HttpServletRequest request) {
		RequestAttributes reqAttr = new RequestAttributes();
		try {
			model.put("orders", search());
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}		
		reqAttr.setQuery(orderListSearchBean.getQuery());
		reqAttr.setFromDate(orderListSearchBean.getFromDate());
		reqAttr.setToDate(orderListSearchBean.getToDate());
		reqAttr.setOrderStatus(orderListSearchBean.getStatus());
		model.put("reqAttr", reqAttr);
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
	
		try {

			orderListSearchBean.setQuery(reqAttr.getQuery());
			orderListSearchBean.setFromDate(reqAttr.getFromDate());
			orderListSearchBean.setToDate(reqAttr.getToDate());
			orderListSearchBean.setStatus(reqAttr.getOrderStatus());
			
			model.put("orders", search());
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}		
		if (reqAttr.getOrderStatus().equals(StatusConstants.ORDER_STATUS_GROUP_INACTIVE)) {
			reqAttr.setInfoMessage("De ordrar som visas är skapta " + propService.getLong(PropertyConstants.MAX_DAYS_INACTIVE_ORDERS_SEARCH).intValue() + " dagar tillbaks i tiden fram tills idag. För att se äldre inaktiva ordrar, sök på specifik status.");
		}
		model.put("reqAttr", reqAttr);
		return "order-list";
	}

	
	private List<OrderHeader> search() throws ParseException {
		Date fromDate = DateUtil.getDefaultStartDate();
		if (!StringUtils.isEmpty(orderListSearchBean.getFromDate())) {
			fromDate = DateUtil.stringToDate(orderListSearchBean.getFromDate());
		}
		
		Date toDate = DateUtil.getTomorrow();
		if (!StringUtils.isEmpty(orderListSearchBean.getToDate())) {
			toDate = DateUtil.stringToDate(orderListSearchBean.getToDate());
		}
		List<OrderHeader> orders;
		Date orderDate = DateUtil.getDefaultStartDate();
		String query = "%" + orderListSearchBean.getQuery() + "%";
		List<String> stati = new ArrayList<String>();
		if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_ACTIVE)){
			stati = Arrays.asList(StatusConstants.ACTIVE_STATI);
		} else if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_INACTIVE)) {
			stati = Arrays.asList(StatusConstants.INACTIVE_STATI);
			orderDate = DateUtil.getStartDateForInactiveOrders(propService.getLong(PropertyConstants.MAX_DAYS_INACTIVE_ORDERS_SEARCH).intValue());
		} else {
			stati.add(orderListSearchBean.getStatus());
		}
		if (StatusUtil.isActiveStatus(orderListSearchBean.getStatus())) {
			orders = orderRepo.findOrdersFromSearch(stati, orderDate, query, sessionBean.getCustomerGroup().getId());
		} else {
			orders = orderRepo.findDeliveredOrdersFromSearch(stati, orderDate, query, fromDate, toDate, sessionBean.getCustomerGroup().getId());
		}
		return orders;
	}
	
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	@Autowired
	public void setOrderListSearchBean(OrderListSearchBean orderListSearchBean) {
		this.orderListSearchBean = orderListSearchBean;
	}
	
}
