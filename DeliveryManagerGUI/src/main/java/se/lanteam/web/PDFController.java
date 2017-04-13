package se.lanteam.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.DocumentException;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.PropertyConstants;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.model.SearchBean;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PDFGenerator;
import se.lanteam.services.PropertyService;

@Controller
public class PDFController {

	private OrderRepository orderRepo;
	private PropertyService propService;
	private CustomerGroupRepository customerRepo;
	private SessionBean sessionBean;
	private SearchBean searchBean;

	@RequestMapping(value = "generate-work-order/{orderId}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource generateWorkOrder(@PathVariable Long orderId, ModelMap model) {
		PDFGenerator gen = new PDFGenerator("pdftemplates/", ".html", propService.getString(PropertyConstants.PDF_IMAGES_FOLDER));
		OrderHeader order = orderRepo.findOne(orderId);

		model.put("order", order);
		CustomerGroup customerGroup = customerRepo.getOne(order.getCustomerGroup().getId()); 
		List<OrderCustomField> orderCustomFields = new ArrayList<OrderCustomField>();
		for (OrderCustomField orderCustomField : order.getOrderCustomFields()) {
			if (showInWorkOrder(customerGroup, orderCustomField)) {
				orderCustomFields.add(orderCustomField);
			}
		}
		
		model.put("orderCustomFields", orderCustomFields);

		try {
			String folder = propService.getString(PropertyConstants.WORK_ORDERS_FOLDER);
			gen.generate(new File(folder + "work-order-" + order.getOrderNumber() + ".pdf"), "work-order", model);			
			return new FileSystemResource(folder + "work-order-" + order.getOrderNumber() + ".pdf");
		} catch (FileNotFoundException e) {
		} catch (DocumentException e) {
		}
		return null;
	}

	private Boolean showInWorkOrder(CustomerGroup customerGroup, OrderCustomField orderCustomField) {
		for (CustomerCustomField customerCustomfield : customerGroup.getCustomerCustomFields()) {
			if (customerCustomfield.getCustomField().getIdentification() 
					== orderCustomField.getCustomField().getIdentification()
					&& customerCustomfield.getShowInWorkNote() == true) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = "generate-delivery-notes", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource generateDeliveryNotes(ModelMap model) {
		PDFGenerator gen = new PDFGenerator("pdftemplates/", ".html", propService.getString(PropertyConstants.PDF_IMAGES_FOLDER));

		model.put("regConfig", sessionBean.getCustomerGroup().getRegistrationConfig());
		List<OrderHeader> orders = searchBean.getOrderList();
		
		List<OrderHeader> completeOrders = new ArrayList<OrderHeader>();
		for (OrderHeader order : orders) {
			OrderHeader completeOrder = orderRepo.getOne(order.getId());
			List<OrderCustomField> orderCustomFields = new ArrayList<OrderCustomField>();
			for (OrderCustomField orderCustomField : completeOrder.getOrderCustomFields()) {
				if (showInDeliveryNote(sessionBean.getCustomerGroup(), orderCustomField)) {
					orderCustomFields.add(orderCustomField);
				}
			}
			completeOrder.setCustomFieldsInDeliveryNote(orderCustomFields);
			completeOrders.add(completeOrder);
		}
		
		model.put("orders", completeOrders);
		
		try {
			String folder = propService.getString(PropertyConstants.DELIVERY_NOTES_FOLDER);
			gen.generate(new File(folder + "delivery-notes-" + DateUtil.getTodayAsString() + ".pdf"), "delivery-notes", model);
			return new FileSystemResource(folder + "delivery-notes-" + DateUtil.getTodayAsString() + ".pdf");
		} catch (FileNotFoundException e) {
		} catch (DocumentException e) {
		}
		return null;
	}

	@RequestMapping(value = "generate-delivery-note/{orderId}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource generateDeliveryNote(@PathVariable Long orderId, ModelMap model) {
		
		List<OrderHeader> orders = new ArrayList<OrderHeader>();		
		orders.add(orderRepo.findOne(orderId));
		searchBean.setOrderList(orders);
		
		return generateDeliveryNotes(model);
	}
	
	
	private Boolean showInDeliveryNote(CustomerGroup customerGroup, OrderCustomField orderCustomField) {
		for (CustomerCustomField customerCustomfield : customerGroup.getCustomerCustomFields()) {
			if (customerCustomfield.getCustomField().getIdentification() 
					== orderCustomField.getCustomField().getIdentification()
					&& customerCustomfield.getShowInDeliveryNote()) {
				return true;
			}
		}
		return false;
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
	public void setCustomerGroupRepo(CustomerGroupRepository customerGroupRepo) {
		this.customerRepo = customerGroupRepo;
	}
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	@Autowired
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
}
