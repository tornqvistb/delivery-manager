package se.lanteam.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.SessionConstants;
import se.lanteam.domain.CustomerCustomField;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.RegistrationConfig;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SearchBean;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.CustomerGroupRepository;

@Controller
public class CustomerGroupController {
	
	private CustomerGroupRepository customerRepo;
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
		System.out.println("size of custom config: " + customerGroup.getCustomerCustomFields().size());
		if (customerGroup.getRegistrationConfig() == null) {
			customerGroup.setRegistrationConfig(new RegistrationConfig());
		}
		model.put("customerGroup", customerGroup);
		return "edit-customer-group";
	}

	@RequestMapping(value = "customer-groups/save-settings", method = RequestMethod.POST)
	public String saveSettings(@ModelAttribute CustomerGroup customerGroup,
			ModelMap model) {
			
		customerGroup.getRegistrationConfig().setCustomerGroup(customerGroup);
		customerGroup.getReportsConfig().setCustomerGroup(customerGroup);
		
		for (CustomerCustomField customerCustomField : customerGroup.getCustomerCustomFields()) {
			customerCustomField.setCustomerGroup(customerGroup);
		}
		
		customerRepo.save(customerGroup);
		sessionBean.setCustomerGroup(customerGroup);
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		RequestAttributes reqAttr = new RequestAttributes();
		reqAttr.setThanksMessage("Kundgrupp " + customerGroup.getName() + " uppdaterad.");
		model.put("reqAttr", reqAttr);
		return "customer-groups";
	}
	
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
}
