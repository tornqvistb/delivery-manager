package se.lanteam.web;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PropertyService;

@Controller
public class OrderListController {
	
	private OrderRepository orderRepo;
	private ErrorRepository errorRepo;
	private PropertyService propService;
	
	@RequestMapping("/")
	public String showStartPage() {
		return "redirect:order-list";
	}
		
	@RequestMapping("order-list")
	public String showOrderList(ModelMap model) {
		List<OrderHeader> orders = orderRepo.findOrdersByStatusList(Arrays.asList(StatusConstants.ACTIVE_STATI));
		model.put("orders", orders);
		model.put("reqAttr", new RequestAttributes(errorRepo.findErrorsByArchived(false).size()));
		return "order-list";
	}
	@RequestMapping(value="order-list/view/{orderId}", method=RequestMethod.GET)
	public String showOrderView(@PathVariable Long orderId, ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		RequestAttributes reqAttr = new RequestAttributes(errorRepo.findErrorsByArchived(false).size());
		model.put("reqAttr", reqAttr);
		return "order-details";
	}
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}
	@Autowired
	public void setPropertyService(PropertyService propService) {
		this.propService = propService;
	}
	@RequestMapping(value="order-list/search", method=RequestMethod.GET)
	public String searchOrders(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		String status = reqAttr.getOrderStatus();
		List<OrderHeader> orders;
		reqAttr = new RequestAttributes(errorRepo.findErrorsByArchived(false).size());
		if (status.equals(StatusConstants.ORDER_STATUS_GROUP_ACTIVE)){ 
			orders = orderRepo.findOrdersByStatusList(Arrays.asList(StatusConstants.ACTIVE_STATI));
		} else if (status.equals(StatusConstants.ORDER_STATUS_GROUP_INACTIVE)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int daysBack = propService.getLong(PropertyConstants.MAX_DAYS_INACTIVE_ORDERS_SEARCH).intValue();
			cal.add(Calendar.DATE, - daysBack);
			Date startDate = cal.getTime(); 
			orders = orderRepo.findOrdersByStatusListAfterDate(Arrays.asList(StatusConstants.INACTIVE_STATI), startDate);
			reqAttr.setInfoMessage("De ordrar som visas är skapta " + daysBack + " dagar tillbaks i tiden fram tills idag. För att se äldre inaktiva ordrar, sök på specifik status.");
		} else {
			orders = orderRepo.findOrdersByStatus(status);
		}		
		model.put("orders", orders);		
		reqAttr.setOrderStatus(status);
		model.put("reqAttr", reqAttr);
		return "order-list";
	}
	
}
