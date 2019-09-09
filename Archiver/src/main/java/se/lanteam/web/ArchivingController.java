package se.lanteam.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class ArchivingController {
	
	private OrderRepository orderRepo;
	private CustomerGroupRepository customerRepo;
	
	@RequestMapping("/")
	public String showStartPage() {
		return "redirect:order-list";
	}
		
	@RequestMapping("order-list")
	public String showOrderList(ModelMap model) {
		List<OrderHeader> orders = new ArrayList<OrderHeader>();
		model.put("orders", orders);
		model.put("reqAttr", new RequestAttributes());
		model.put("customerGroups", customerRepo.findAll());
		return "order-list";
	}
	@RequestMapping(value="view/{orderId}", method=RequestMethod.GET)
	public String showOrderView(@PathVariable Long orderId, ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		model.put("order", order);
		RequestAttributes reqAttr = new RequestAttributes();
		model.put("reqAttr", reqAttr);
		return "order-details";
	}
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
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
			
			List<OrderHeader> orders;
			List<String> stati = new ArrayList<String>();
			stati.add(StatusConstants.ORDER_STATUS_TRANSFERED);
			Pageable maxRows = new PageRequest(0, 500);
			orders = orderRepo.findDeliveredOrdersFromSearch(stati, query, fromDate, toDate, reqAttr.getCustomerGroupId(), maxRows);
			model.put("orders", orders);
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt inmatade datum");
		}		
		reqAttr.setOrderStatus(status);
		model.put("customerGroups", customerRepo.findAll());
		model.put("reqAttr", reqAttr);
		return "order-list";
	}
	@ResponseBody
	@RequestMapping(value="order-list/view/viewfile/{orderId}", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] downloadFile(
			@PathVariable Long orderId) throws IOException {
		OrderHeader order = orderRepo.findOne(orderId);
		return order.getAttachment().getFileContent();
	}

}
