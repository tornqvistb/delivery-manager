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

import se.lanteam.constants.PropertyConstants;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.OrderCustomField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.OrderRepository;
import se.lanteam.services.PDFGenerator;
import se.lanteam.services.PropertyService;

@Controller
public class PDFController {

	private OrderRepository orderRepo;
	private PropertyService propService;
	private CustomerGroupRepository customerRepo;

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
	
	@RequestMapping(value = "generate-delivery-note/{orderId}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource generateDeliveryNote(@PathVariable Long orderId, ModelMap model) {
		PDFGenerator gen = new PDFGenerator("pdftemplates/", ".html", propService.getString(PropertyConstants.PDF_IMAGES_FOLDER));
		OrderHeader order = orderRepo.findOne(orderId);

		model.put("order", order);

		try {
			String folder = propService.getString(PropertyConstants.DELIVERY_NOTES_FOLDER);
			gen.generate(new File(folder + "delivery-note-" + order.getOrderNumber() + ".pdf"), "delivery-note", model);
			return new FileSystemResource(folder + "delivery-note-" + order.getOrderNumber() + ".pdf");
		} catch (FileNotFoundException e) {
		} catch (DocumentException e) {
		}
		return null;
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

}
