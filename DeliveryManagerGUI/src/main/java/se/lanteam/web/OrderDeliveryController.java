package se.lanteam.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ListUtils;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.StatusConstants;
import se.lanteam.domain.Attachment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.DeliveryForm;
import se.lanteam.model.DeliveryStatus;
import se.lanteam.model.SessionBean;

@Controller
public class OrderDeliveryController extends BaseController {
	
	private SessionBean sessionBean;		

	private static final String DELIVERY_FORM_BEAN = "deliveryForm";
	private static final String DELIVERY_SEARCH_VIEW = "delivery-search";
	private static final String DELIVERY_CONFIRM_VIEW = "delivery-confirm";
			
	private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryController.class);
	
	@RequestMapping("delivery-mobile")
	public String showOrderList(ModelMap model, HttpServletRequest request) {
		DeliveryForm form = new DeliveryForm();
		form.setPlannedOrders(getPlannedOrders(form.getOrdersAsList()));
		model.put(DELIVERY_FORM_BEAN, form);
		return DELIVERY_SEARCH_VIEW;
	}
	
	private List<OrderHeader> getPlannedOrders(List<String> selectedOrders) {
		List<String> stati = new ArrayList<>();
		stati.add(StatusConstants.ORDER_STATUS_ROUTE_PLANNED);
		stati.add(StatusConstants.ORDER_STATUS_REGISTRATION_DONE);
		List<OrderHeader> plannedOrdersToday = orderRepo.findOrdersByPlanDateAndStatus(stati, DateUtil.getTodayAtMidnight(), sessionBean.getCustomerGroup().getId());

		
		
		Iterator<OrderHeader> it = plannedOrdersToday.iterator();
		while (it.hasNext()) {
			OrderHeader oh = it.next();
			// Visa inte underordrar i samleverans
			if (oh.isChildOrderInJoint()) {
				   it.remove();
			}
			if (!ListUtils.isEmpty(selectedOrders)) {
				for (String selectedOrder : selectedOrders) {
					if (oh.getOrderNumber().equals(selectedOrder)) {
					   it.remove();
					   break;
					}
				}
			}
			
		}			
		return plannedOrdersToday;
	}
	
	@RequestMapping(value="delivery-mobile/search", method=RequestMethod.GET)
	public String searchOrders(ModelMap model, @ModelAttribute DeliveryForm form) {	
		logger.debug("Search string: " + form.getQuery());
		List<OrderHeader> orders = orderRepo.findOrdersByOrderNumber(form.getQuery());
		if (orders.isEmpty()) {
			return toSearchWithError(form, "Ordern hittades inte");
		}		
		OrderHeader order = orders.get(0);
		if (order.getCustomerGroup().getId() != sessionBean.getCustomerGroup().getId()) {
			return toSearchWithError(form, "Ordern tillhör inte aktuell kundgrupp, tillhör " + order.getCustomerGroup().getName() + ".");
		}
		if (!order.getOkToDeliverWithApp()) {
			return toSearchWithError(form, "Ordern har fel status för leverans: " + order.getStatusDisplay() + ".");
		}
		if (form.getOrderNumbersConcat().contains(order.getOrderNumber())) {
			return toSearchWithError(form, "Ordern är redan tillagd i listan.");
		}
		if (order.isChildOrderInJoint()) {
			String mainOrder = order.getJointDelivery();
			if (form.getOrderNumbersConcat().contains(mainOrder)) {
				return toSearchWithError(form, "Order " + order.getOrderNumber() 
				+ " ingår i samleverans för order "+ mainOrder + " som redan är tillagd i listan.");
			} else {
				return toSearchWithSuccess(mainOrder, form, "Order " + order.getOrderNumber() 
				+ " ingår i samleverans för order "+ mainOrder + ". Huvudorder tillagd.");
			}
		}
		
		// Allt OK
		return toSearchWithSuccess(order.getOrderNumber(), form, "Order " + order.getOrderNumber() 
		+ " hittades och lades till i listan.");
	}

	private String toSearchWithSuccess(String orderNumber, DeliveryForm form, String msg) {
		form.setOrderNumber(orderNumber);
		form.addOrderNumber(orderNumber);
		form.setPlannedOrders(getPlannedOrders(form.getOrdersAsList()));
		form.setInfoMessage(msg);
		form.setQuery("");
		return DELIVERY_SEARCH_VIEW;
	}
	
	private String toSearchWithError(DeliveryForm form, String msg) {
		form.setPlannedOrders(getPlannedOrders(form.getOrdersAsList()));
		form.setErrorMessage(msg);
		return DELIVERY_SEARCH_VIEW;
	}
	
	@RequestMapping(value="delivery-mobile/del-order/{orderNumber}/{orderNumbersConcat}", method=RequestMethod.GET)
	public String deleteOrder(@PathVariable String orderNumber, @PathVariable String orderNumbersConcat, ModelMap model) {
		DeliveryForm form = new DeliveryForm();
		form.setOrderNumbersConcat(orderNumbersConcat);
		form.removeOrderNumber(orderNumber);
		form.setPlannedOrders(getPlannedOrders(form.getOrdersAsList()));
		form.setInfoMessage("Order " + orderNumber + " togs bort från listan");
		model.put(DELIVERY_FORM_BEAN, form);
		return DELIVERY_SEARCH_VIEW;
	}

	@RequestMapping(value="delivery-mobile/add-order/{orderNumber}", method=RequestMethod.GET)
	public String addOrder(@PathVariable String orderNumber, ModelMap model) {
		return addOrder(orderNumber, "", model);
	}
	
	@RequestMapping(value="delivery-mobile/add-order/{orderNumber}/{orderNumbersConcat}", method=RequestMethod.GET)
	public String addOrder(@PathVariable String orderNumber, @PathVariable String orderNumbersConcat, ModelMap model) {
		DeliveryForm form = new DeliveryForm();
		form.setOrderNumbersConcat(orderNumbersConcat);
		form.addOrderNumber(orderNumber);
		form.setPlannedOrders(getPlannedOrders(form.getOrdersAsList()));
		form.setInfoMessage("Order " + orderNumber + " lades till i listan");
		model.put(DELIVERY_FORM_BEAN, form);
		return DELIVERY_SEARCH_VIEW;
	}
	
	@RequestMapping(value="delivery-mobile/del-order-confirm/{orderNumber}/{orderNumbersConcat}", method=RequestMethod.GET)
	public String deleteOrderConfirmDlg(@PathVariable String orderNumber, @PathVariable String orderNumbersConcat, ModelMap model) {
		DeliveryForm form = new DeliveryForm();
		form.setOrderNumbersConcat(orderNumbersConcat);
		form.removeOrderNumber(orderNumber);
		form.setInfoMessage("Order togs bort från listan");
		if (form.getOrdersAsList().isEmpty()) {
			model.put(DELIVERY_FORM_BEAN, form);
			return DELIVERY_SEARCH_VIEW;
		} else {
			getReceivers(form);
			form.setStatiFromOrderList();		
			model.put(DELIVERY_FORM_BEAN, form);
			return DELIVERY_CONFIRM_VIEW;
		}		
	}

	@RequestMapping(value="delivery-mobile/back-to-search/{orderNumbersConcat}", method=RequestMethod.GET)
	public String backToSearch(@PathVariable String orderNumbersConcat, ModelMap model) {
		DeliveryForm form = new DeliveryForm();
		form.setOrderNumbersConcat(orderNumbersConcat);
		model.put(DELIVERY_FORM_BEAN, form);
		return DELIVERY_SEARCH_VIEW;
	}
	
	@RequestMapping(value="delivery-mobile/to-confirmation/{orderNumbersConcat}", method=RequestMethod.GET)
	public String toConfirmation(@PathVariable String orderNumbersConcat, ModelMap model) {
		DeliveryForm form = new DeliveryForm();
		form.setOrderNumbersConcat(orderNumbersConcat);
		getReceivers(form);
		form.setStatiFromOrderList();
		model.put(DELIVERY_FORM_BEAN, form);
		return DELIVERY_CONFIRM_VIEW;
	}

	private DeliveryForm getReceivers(DeliveryForm form) {
		List<OrderHeader> orderList = new ArrayList<>();
		for (String orderNumber : form.getOrdersAsList()) {
			orderList.add(orderRepo.findOrdersByOrderNumber(orderNumber).get(0));
		}
		form.setReceiversFromOrderList(orderList);
		return form;
	}
	
	@RequestMapping(value="delivery-mobile/confirm", method=RequestMethod.POST)
	public String confirmDeliver(ModelMap model, @ModelAttribute DeliveryForm form,
			@RequestParam("file") MultipartFile multipartFile) throws IOException {			
		logger.debug("form: " + form.toString());
		logger.debug("size signature: " + form.getSignature().length());
		//Loopa igenom ordrar
		/*
		for (String order : form.getOrdersAsList()) {
			List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(order);
			if (!orderList.isEmpty()) {
				OrderHeader oh = orderList.get(0);
				oh.setDeliveryStatus(form.getDeliveryStatus());
				if (DeliveryStatus.STATUS_DELIVERED.equals(form.getDeliveryStatus())) {
					oh.setDeliverySignature(form.getSignature());
					if (DeliveryForm.OTHER_PERSON.equals(form.getReceiver())) {
						oh.setDeliveryReceiverName(form.getNameClarification());
					} else {
						oh.setDeliveryReceiverName(form.getReceiver());
					}
					oh.setStatus(StatusConstants.ORDER_STATUS_SENT);
				} else {
					oh.setDeliveryComment(form.getComment());
				}
				if (multipartFile != null) {
					Attachment attachment = new Attachment();
					attachment.setOrderHeader(oh);
					attachment.setFileContent(multipartFile.getBytes());
					attachment.setFileName(multipartFile.getOriginalFilename());
					attachment.setFileSize(multipartFile.getSize());
					attachment.setContentType("image/jpeg");
					oh.setAttachment(attachment);
				}
				orderRepo.save(oh);
			}			 
		}
		*/
		
		List<OrderHeader> ordersToDeliver = getOrdersToDeliverIncludingSubOrders(form.getOrdersAsList());
		for (OrderHeader oh : ordersToDeliver) {
			oh.setDeliveryStatus(form.getDeliveryStatus());
			if (DeliveryStatus.STATUS_DELIVERED.equals(form.getDeliveryStatus())) {
				oh.setDeliverySignature(form.getSignature());
				if (DeliveryForm.OTHER_PERSON.equals(form.getReceiver())) {
					oh.setDeliveryReceiverName(form.getNameClarification());
				} else {
					oh.setDeliveryReceiverName(form.getReceiver());
				}
				oh.setStatus(StatusConstants.ORDER_STATUS_SENT);
			} else {
				oh.setStatus(StatusConstants.ORDER_STATUS_DELIVERY_ERROR);
				oh.setDeliveryComment(form.getComment());
			}
			if (multipartFile != null) {
				Attachment attachment = new Attachment();
				attachment.setOrderHeader(oh);
				attachment.setFileContent(multipartFile.getBytes());
				attachment.setFileName(multipartFile.getOriginalFilename());
				attachment.setFileSize(multipartFile.getSize());
				attachment.setContentType("image/jpeg");
				oh.setAttachment(attachment);
			}
			orderRepo.save(oh);
		}
		//
		form = new DeliveryForm();
		form.setInfoMessage("Leverans OK");
		form.setPlannedOrders(getPlannedOrders(null));
		model.put(DELIVERY_FORM_BEAN, form);
		return DELIVERY_SEARCH_VIEW;
	}
		
	private List<OrderHeader> getOrdersToDeliverIncludingSubOrders(List<String> orderNumbers) {
		List<OrderHeader> orders = new ArrayList<>();
		for (String orderNumber : orderNumbers) {
			List<OrderHeader> orderList = orderRepo.findOrdersByOrderNumber(orderNumber);
			if (!orderList.isEmpty()) {
				OrderHeader oh = orderList.get(0);
				orders.add(oh);
				for (String childOrder : oh.getJoinedOrdersAsList()) {
					orderList = orderRepo.findOrdersByOrderNumber(childOrder);
					if (!orderList.isEmpty()) {
						oh = orderList.get(0);
						orders.add(oh);
					}
				}
			}
		}
		return orders;
	}
	
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
}
