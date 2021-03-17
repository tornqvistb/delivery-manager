package se.lanteam.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.OrderListSearchBean;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;
import se.lanteam.services.PropertyService;

@Controller
public class OrderListController extends BaseController {
	
	private PropertyService propService;
	private SessionBean sessionBean;
	private OrderListSearchBean orderListSearchBean;
	private static final Boolean SHOW_MAIN_ORDERS = false;
	private static final Boolean SHOW_SUB_ORDERS = true;

	
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
		RequestAttributes reqAttr = new RequestAttributes();
		reqAttr = addRelatedOrders(reqAttr, order);
		reqAttr = addThanksMessage(reqAttr, order);
		reqAttr = addRoutePlanning(reqAttr, order);
		model.put("reqAttr", reqAttr);
		model.put("order", order);
		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		model.put("deliveryAreas", deliveryAreaRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
		return "order-details";
	}
	@RequestMapping(value="order-list/viewbyon/{orderNumber}", method=RequestMethod.GET)
	public String showOrderViewByON(@PathVariable String orderNumber, ModelMap model) {
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(orderNumber);
		if (orders != null && orders.size() > 0) {
			return showOrderView(orders.get(0).getId(), model);
		} else {
			return "error";
		}
	}

	@Autowired
	public void setPropertyService(PropertyService propService) {
		this.propService = propService;
	}
	@RequestMapping(value="order-list/search", method=RequestMethod.GET)
	public String searchOrders(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {		
	
		try {

			orderListSearchBean.setQuery(reqAttr.getQuery());
			orderListSearchBean.setFromDate(reqAttr.getFromDate());
			orderListSearchBean.setToDate(reqAttr.getToDate());
			orderListSearchBean.setStatus(reqAttr.getOrderStatus());
			if (!datesAreEmpty()) {
				reqAttr.setInfoMessage("Då ni angav leveransdatum, så visas endast levererade ordrar, oavsett vilken status ni valt i sökfiltret.");				
			}
			model.put("orders", search());
			reqAttr.setFromDate(orderListSearchBean.getFromDate());
			reqAttr.setToDate(orderListSearchBean.getToDate());
			reqAttr.setOrderStatus(orderListSearchBean.getStatus());
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}
		model.put("reqAttr", reqAttr);
		return "order-list";
	}

	
	private List<OrderHeader> search() throws ParseException {
		String query = "%" + orderListSearchBean.getQuery() + "%";
		populateDatesInBean();
		List<String> stati = new ArrayList<String>();
		Pageable maxRows = new PageRequest(0, propService.getLong(PropertyConstants.MAX_ORDERS_IN_SEARCH).intValue());		
		if (datesAreEmpty()) {
			Boolean showSubOrders = false;
			if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_ACTIVE)){
				stati = Arrays.asList(StatusConstants.ACTIVE_STATI);
			} else if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_INACTIVE)) {
				stati = Arrays.asList(StatusConstants.INACTIVE_STATI);
				showSubOrders = true;
			} else if (orderListSearchBean.getStatus().equals(StatusConstants.ORDER_STATUS_GROUP_ALL)) {
				stati = Arrays.asList(StatusConstants.ALL_STATI);
				showSubOrders = true;
			} else {
				stati.add(orderListSearchBean.getStatus());
				if (Arrays.asList(StatusConstants.ALL_STATI).contains(orderListSearchBean.getStatus())) {
					showSubOrders = true;
				}
			}			
			return orderRepo.findOrdersFromSearch(stati, query, sessionBean.getCustomerGroup().getId(), getFlagValuesForMainAndSuborders(showSubOrders), maxRows);
		} else {
			if (!Arrays.asList(StatusConstants.INACTIVE_STATI).contains(orderListSearchBean.getStatus())) {
				orderListSearchBean.setStatus(StatusConstants.ORDER_STATUS_GROUP_INACTIVE);
				stati = Arrays.asList(StatusConstants.INACTIVE_STATI);
			} else {
				stati.add(orderListSearchBean.getStatus());
			}
			return orderRepo.findDeliveredOrdersFromSearch(stati, query, DateUtil.stringToDate(orderListSearchBean.getFromDate()), DateUtil.stringToDate(orderListSearchBean.getToDate()), sessionBean.getCustomerGroup().getId(), maxRows);
		}
	}
	
	private List<Boolean> getFlagValuesForMainAndSuborders(Boolean showSubOrders) {
		List<Boolean> includeList = new ArrayList<Boolean>();
		includeList.add(SHOW_MAIN_ORDERS);
		if (showSubOrders) {
			includeList.add(SHOW_SUB_ORDERS);
		}
		return includeList;
	}
	
	private void populateDatesInBean() {
		if (!datesAreEmpty()) {
			if (StringUtils.isEmpty(orderListSearchBean.getFromDate())) {
				orderListSearchBean.setFromDate(DateUtil.getDefaultStartDateAsString());
			}
			if (StringUtils.isEmpty(orderListSearchBean.getToDate())) {
				orderListSearchBean.setToDate(DateUtil.getTomorrowAsString());
			}
		}
	}
	
	private boolean datesAreEmpty() {
		return StringUtils.isEmpty(orderListSearchBean.getFromDate())
				&& StringUtils.isEmpty(orderListSearchBean.getToDate());
	}
	
	private RequestAttributes addThanksMessage(RequestAttributes reqAttr, OrderHeader order) {
		if (order.getStatus().equals(StatusConstants.ORDER_STATUS_SENT_CUSTOMER)) {
			reqAttr.setThanksMessage("Orderinfo kommer att skickas till kund. Inväntar leveransdokument.");
		}
		return reqAttr;
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
