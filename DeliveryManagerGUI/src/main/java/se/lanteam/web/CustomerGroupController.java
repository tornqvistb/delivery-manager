package se.lanteam.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.CustomerGroup;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.services.PropertyService;

@Controller
public class CustomerGroupController {
	
	private CustomerGroupRepository customerRepo;
	private ErrorRepository errorRepo;
	private PropertyService propService;
	
	@RequestMapping("customer-groups")
	public String showCustomerList(ModelMap model) {
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		model.put("reqAttr", new RequestAttributes(errorRepo.findErrorsByArchived(false).size()));
		return "customer-groups";
	}


	
	@RequestMapping(value="customer-groups/activate/{customerId}", method=RequestMethod.GET)
	public String showOrderView(@PathVariable Long customerId, ModelMap model, HttpServletRequest request) {			
		//CustomerGroup customer = customerRepo.findOne(customerId);
		RequestAttributes reqAttr = new RequestAttributes(errorRepo.findErrorsByArchived(false).size());
		model.put("reqAttr", reqAttr);
		CustomerGroup customer = customerRepo.findOne(customerId);
		request.getSession().setAttribute("customerGroupId", customerId);
		request.getSession().setAttribute("customerGroup", customer.getName());
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		return "customer-groups";
	}
	
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}
	@Autowired
	public void setPropertyService(PropertyService propService) {
		this.propService = propService;
	}
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	
}
