package se.lanteam.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.SessionConstants;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.repository.CustomerGroupRepository;

@Controller
public class CustomerGroupController {
	
	private CustomerGroupRepository customerRepo;
	
	@RequestMapping("customer-groups")
	public String showCustomerList(ModelMap model) {
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		return "customer-groups";
	}


	
	@RequestMapping(value="customer-groups/activate/{customerId}", method=RequestMethod.GET)
	public String showOrderView(@PathVariable Long customerId, ModelMap model, HttpServletRequest request) {			
		CustomerGroup customerGroup = customerRepo.findOne(customerId);
		request.getSession().setAttribute(SessionConstants.CURRENT_CUSTOMER_GROUP, customerGroup);
		List<CustomerGroup> customers = customerRepo.findAll();		
		model.put("customerGroups", customers);
		return "customer-groups";
	}
	
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	
}
