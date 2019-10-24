package se.lanteam.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.SessionConstants;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.SystemUser;
import se.lanteam.model.RequestAttributes;
import se.lanteam.model.SessionBean;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.SystemUserRepository;

@Controller
public class LoginController extends BaseController{
	
	private SystemUserRepository userRepo;
	private SessionBean sessionBean;
	
	@RequestMapping("login")
	public String login(ModelMap model) {
		model.put("customerGroups", customerRepo.findAll());
		model.put("reqAttr", new RequestAttributes());
		return "login";
	}
	@RequestMapping(value = "login/confirm", method = RequestMethod.POST)
	public String confirmLogin(@ModelAttribute RequestAttributes reqAttr, ModelMap model, HttpServletRequest request) {
		String validationResult = validate(reqAttr);
		if ("ok".equals(validationResult)) {			
			CustomerGroup customer = customerRepo.findOne(reqAttr.getCustomerId());
			request.getSession().setAttribute(SessionConstants.CURRENT_CUSTOMER_GROUP, customer);
			sessionBean.setCustomerGroup(customer);
			SystemUser user = userRepo.findByUserName(reqAttr.getUserName());
			sessionBean.setSystemUser(user);
			request.getSession().setAttribute(SessionConstants.SYSTEM_USER, user);
			return "redirect:/order-list";
		} else {
			reqAttr = new RequestAttributes();
			model.put("customerGroups", customerRepo.findAll());
			reqAttr.setErrorMessage(validationResult);
			model.put("reqAttr", reqAttr);
			return "login";	
		}		
	}
	
	private String validate(RequestAttributes reqAttr) {
		if (reqAttr.getCustomerId() <= 0) {
			return "Välj kundgrupp";
		}
		SystemUser user = userRepo.findByUserName(reqAttr.getUserName());
		if (user == null) {
			return "Användare finns ej";
		}
		if (!user.getPassword().equals(reqAttr.getPassword())) {
			return "Felaktigt lösenord";
		}		
		return "ok";
	}
	
	@Autowired
	public void setCustomerGroupRepo(CustomerGroupRepository customerRepo) {
		this.customerRepo = customerRepo;
	}
	@Autowired
	public void setSystemUserRepo(SystemUserRepository userRepo) {
		this.userRepo = userRepo;
	}
	@Autowired
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
}
