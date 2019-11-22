package se.lanteam.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.SessionConstants;
import se.lanteam.domain.CustomField;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.DeliveryReportField;
import se.lanteam.domain.OrderHeader;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.CustomFieldRepository;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.ReportsConfigRepository;

@Controller
public class CustomerGroupController extends BaseController{
	
	private CustomFieldRepository customFieldRepo;
	private CustomerGroupRepository customerRepo;
	private ReportsConfigRepository reportsConfigRepo;
	private SessionBean sessionBean;
	
	@RequestMapping("customer-groups")
	public String showCustomerList(ModelMap model) {
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		model.put("reqAttr", new RequestAttributes());
		return "customer-groups";
	}	
	@RequestMapping("choose-customer-group")
	public String chooseCustomerGroup(ModelMap model) {
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		model.put("reqAttr", new RequestAttributes());
		return "choose-customer-group";
	}	
	@RequestMapping(value="customer-groups/activate/{customerId}/{toStartPage}", method=RequestMethod.GET)
	public String activateCustomergroup(@PathVariable Long customerId, @PathVariable String toStartPage, ModelMap model, HttpServletRequest request) {			
		CustomerGroup customerGroup = customerRepo.findOne(customerId);
		request.getSession().setAttribute(SessionConstants.CURRENT_CUSTOMER_GROUP, customerGroup);
		sessionBean.setCustomerGroup(customerGroup);
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		RequestAttributes reqAttr = new RequestAttributes();
		reqAttr.setThanksMessage("Kundgrupp " + customerGroup.getName() + " aktiverad.");
		model.put("reqAttr", reqAttr);
		if ("yes".equals(toStartPage)) {
			return "order-list";
		} else {
			return "customer-groups";
		}
	}

	@RequestMapping(value="customer-groups/settings/{customerId}", method=RequestMethod.GET)
	public String editCustomerGroup(@PathVariable Long customerId, ModelMap model, HttpServletRequest request) {			
		CustomerGroup customerGroup = customerRepo.findOne(customerId);
		if (customerGroup.getRegistrationConfig() == null) {
			customerGroup.setRegistrationConfig(new RegistrationConfig());
		}
		model.put("customerGroup", customerGroup);
		return "edit-customer-group";
	}

	@RequestMapping(value="customer-groups/report-settings/{customerId}", method=RequestMethod.GET)
	public String editCustomerGroupReportSettings(@PathVariable Long customerId, ModelMap model, HttpServletRequest request) {			
		CustomerGroup customerGroup = customerRepo.findOne(customerId);
		RequestAttributes reqAttr = new RequestAttributes();
		if (customerGroup.getRegistrationConfig() == null) {
			customerGroup.setRegistrationConfig(new RegistrationConfig());
		}		
		List<DeliveryReportField> activeFields = new ArrayList<DeliveryReportField>();
		List<DeliveryReportField> inactiveFields = new ArrayList<DeliveryReportField>();
		for (DeliveryReportField field : customerGroup.getReportsConfig().getReportFields()) {
			if (field.getShowInReport()) {
				activeFields.add(field);
			} else {
				inactiveFields.add(field);
			}
		}			
		reqAttr.setActiveReportFields(activeFields);
		reqAttr.setInactiveReportFields(inactiveFields);
		List<DeliveryReportField> allFields = new ArrayList<DeliveryReportField>();
		allFields.addAll(activeFields);
		allFields.addAll(inactiveFields);
		reqAttr.setAllReportFields(allFields);
		reqAttr.setCustomerId(customerId);
		
		reqAttr.setSortByColumn(customerGroup.getReportsConfig().getSortColumnDeliverReport());
		model.put("customerGroup", customerGroup);
		model.put("reqAttr", reqAttr);
		return "edit-customer-report-settings";
	}
	
	@RequestMapping(value="customer-groups/delete/{customerId}", method=RequestMethod.GET)
	public String deleteCustomerGroup(@PathVariable Long customerId, ModelMap model, HttpServletRequest request) {
		RequestAttributes reqAttr = new RequestAttributes();
		CustomerGroup customerGroup = customerRepo.findOne(customerId);
		List<OrderHeader> orders = orderRepo.findOrdersByCustGroup(customerGroup.getId());
		if (orders != null && orders.size() > 0) {
			reqAttr.setErrorMessage("Kundgrupp " + customerGroup.getName() + " kunde ej tas bort. Det finns <strong>" + orders.size() + " st</strong> ordrar kopplade till denna kundgrupp");
		} else {
			customerRepo.delete(customerGroup);
			reqAttr.setThanksMessage("Kundgrupp " + customerGroup.getName() + " borttagen.");	
		}
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		model.put("reqAttr", reqAttr);
		return "customer-groups";
	}

	
	@RequestMapping(value="customer-groups/addnew", method=RequestMethod.POST)
	public String addCustomerGroup(ModelMap model, HttpServletRequest request) {			
		CustomerGroup customerGroup = new CustomerGroup();
		customerGroup.setRegistrationConfig(new RegistrationConfig());
		customerGroup.setCustomerCustomFields(getDefaultCustomFields(customerGroup));
		model.put("customerGroup", customerGroup);
		return "edit-customer-group";
	}

	private List<CustomerCustomField> getDefaultCustomFields(CustomerGroup customerGroup) {
		List<CustomerCustomField> customerCustomFields = new ArrayList<CustomerCustomField>();
		List<CustomField> allcustomFields = customFieldRepo.findAll();
		for (CustomField customField : allcustomFields) {
			customerCustomFields.add(new CustomerCustomField(customField, customerGroup));
		}
		return customerCustomFields;
	}
	
	@RequestMapping(value = "customer-groups/save-settings", method = RequestMethod.POST)
	public String saveSettings(@ModelAttribute CustomerGroup customerGroup,
			ModelMap model) {
		RequestAttributes reqAttr = new RequestAttributes();
		if (!StringUtils.isEmpty(customerGroup.getName())) {
			
			customerGroup.getRegistrationConfig().setCustomerGroup(customerGroup);
			customerGroup.getReportsConfig().setCustomerGroup(customerGroup);
			
			for (CustomerCustomField customerCustomField : customerGroup.getCustomerCustomFields()) {
				customerCustomField.setCustomerGroup(customerGroup);
				customerCustomField.setCustomField(customFieldRepo.getOne(customerCustomField.getCustomField().getIdentification()));
			}
			
			customerRepo.save(customerGroup);
			sessionBean.setCustomerGroup(customerGroup);
			List<CustomerGroup> customers = customerRepo.findAll();		
			model.put("customerGroups", customers);
			
			reqAttr.setThanksMessage("Kundgrupp " + customerGroup.getName() + " uppdaterad.");
		} else {
			reqAttr.setErrorMessage("Namn på kundgruppen obligatoriskt");
		}
		model.put("reqAttr", reqAttr);
		return "customer-groups";
	}

	@RequestMapping(value = "customer-groups/save-delivery-report-settings", method = RequestMethod.POST)
	public String saveDeliveryReportSettings(@ModelAttribute RequestAttributes reqAttr,
			ModelMap model) {
		CustomerGroup customerGroup = customerRepo.findOne(reqAttr.getCustomerId());
		
		Set<DeliveryReportField> deliveryReportFields = new HashSet<DeliveryReportField>();
		List<String> listFromForm = new ArrayList<String>();
		String[] arrFromForm = reqAttr.getListValues().split(";");
		if (arrFromForm != null) {
			listFromForm = Arrays.asList(arrFromForm);
		}
		for (DeliveryReportField field : customerGroup.getReportsConfig().getReportFields()) {
			deliveryReportFields.add(getField(listFromForm, field));
		}		
		customerGroup.getReportsConfig().setReportFields(deliveryReportFields);
		customerGroup.getReportsConfig().setSortColumnDeliverReport(reqAttr.getSortByColumn());
		customerRepo.save(customerGroup);		
		return "redirect:/customer-groups/report-settings/" + reqAttr.getCustomerId();
	}

	private DeliveryReportField getField(List<String> formList, DeliveryReportField field) {
		Long columnIdx = 1L;
		for (String fieldName : formList) {
			if (fieldName.equals(field.getFieldName())) {
				field.setShowInReport(true);
				field.setColumnNumber(columnIdx);
				return field;
			}
			columnIdx++;
		}
		field.setShowInReport(false);
		field.setColumnNumber(0L);
		return field;
	}
	
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	@Autowired
	public void setCustomFieldRepo(CustomFieldRepository customFieldRepo) {
		this.customFieldRepo = customFieldRepo;
	}
	@Autowired
	public void setReportsConfigRepo(ReportsConfigRepository reportsConfigRepo) {
		this.reportsConfigRepo = reportsConfigRepo;
	}
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
}
