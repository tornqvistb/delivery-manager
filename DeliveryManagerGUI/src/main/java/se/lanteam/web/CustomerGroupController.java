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
import se.lanteam.domain.ReportsConfig;
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
		List<DeliveryReportField> activeFields = customerGroup.getReportsConfig().getActiveReportFields();
		List<DeliveryReportField> inactiveFields = customerGroup.getReportsConfig().getInactiveReportFields();
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
			// if no delivery reports configuration, add a new one
			if (customerGroup.getReportsConfig().getReportFields().isEmpty()) {
				customerGroup.getReportsConfig().setReportFields(getDeliveryReportFields(customerGroup.getReportsConfig()));
				customerGroup.getReportsConfig().setSortColumnDeliverReport("ohOrderNumber");
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

	private Set<DeliveryReportField> getDeliveryReportFields(ReportsConfig config) {
		Set<DeliveryReportField> fields = new HashSet<>();
		
		Long l = 1L;
		
		fields.add(new DeliveryReportField(config, "ohOrderNumber", "Ordernummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomerOrderNumber", "Kundens ordernummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomerSalesOrder", "Kundens säljnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohNetsetOrderNumber", "Web-ordernummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohLeasingNumber", "Orderhuvud leasingnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomerName", "Kundens namn", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomerNumber", "Kundnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohDeliveryAddress", "Leveransadress", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohDeliveryCity", "Leveransort", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohDeliveryDate", "Leveransdatum", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohDeliveryPostalAddress1", "Leveransadress 1", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohDeliveryPostalAddress2", "Leveransadress 2", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohDeliveryPostalCode", "Leverans postnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCity", "Postort", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohPostalAddress1", "Postadress 1", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohPostalAddress2", "Postadress 2", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohPostalCode", "Postnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohContact1Email", "Kontaktperson 1 epost", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohContact1Name", "Kontaktperson 1 namn", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohContact1Phone", "Kontaktperson 1 telefon", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohContact2Email", "Kontaktperson 2 epost", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohContact2Name", "Kontaktperson 2 namn", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohContact2Phone", "Kontaktperson 2 telefon", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohStatus", "Orderstatus", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohJointDelivery", "Samleverans", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohOrderDate", "Orderdatum", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohTransferDate", "Datum för leveransavisering", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField1", "Order extrafält 1", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField2", "Order extrafält 2", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField3", "Order extrafält 3", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField4", "Order extrafält 4", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField5", "Order extrafält 5", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField6", "Order extrafält 6", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField7", "Order extrafält 7", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField8", "Order extrafält 8", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField9", "Order extrafält 9", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "ohCustomField10", "Order extrafält 10", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olArticleNumber", "Artikelnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olArticleDescription", "Artikelbeskrivning", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olRowNumber", "Radnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olCustomerRowNumber", "Kundens radnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olInstallationType", "Installationstyp", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olOperatingSystem", "Operativsystem", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olOrganisationUnit", "OU", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olRegistered", "Registrerat antal", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olRemaining", "Kvarvarande antal", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olTotal", "Totalt antal", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olLeasingNumber", "Leasingnummer orderrad", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "olRITM", "RITM-nummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqSerialNo", "Serienummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqStealingTag", "Stöldskyddsnummer", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute1", "Kundattribut 1", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute2", "Kundattribut 2", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute3", "Kundattribut 3", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute4", "Kundattribut 4", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute5", "Kundattribut 5", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute6", "Kundattribut 6", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute7", "Kundattribut 7", true, l, l));l++;
		fields.add(new DeliveryReportField(config, "eqCustomAttribute8", "Kundattribut 8", true, l, l));		
		
		return fields;
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
