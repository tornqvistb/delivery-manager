package se.lanteam.web;

import java.text.ParseException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.constants.DateUtil;
import se.lanteam.constants.StatusConstants;
import se.lanteam.model.RequestAttributes;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.OrderRepository;

@Controller
public class ArchivingController {
	
	private ErrorRepository errorRepo;
	private OrderRepository orderRepo;
	
	@RequestMapping("archiving")
	public String showArchiving(ModelMap model) {
		RequestAttributes reqAttr = new RequestAttributes();
		model.put("reqAttr", refreshArchiveInfo(reqAttr));		
		return "archiving";
	}
	@RequestMapping(value="archiving/do-archive", method=RequestMethod.POST)
	public String searchOrders(ModelMap model, @ModelAttribute RequestAttributes reqAttr) {
		
		try {
			Integer count = orderRepo.countOrdersForArchiving(DateUtil.stringToDate(reqAttr.getFromDate()), StatusConstants.ORDER_STATUS_TRANSFERED);
			orderRepo.setArchiving(true, DateUtil.stringToDate(reqAttr.getFromDate()), StatusConstants.ORDER_STATUS_TRANSFERED);
			reqAttr.setThanksMessage("Arkivering initierad för " + count +" st order/ordrar.");			
		} catch (ParseException e) {
			reqAttr.setErrorMessage("Felaktigt angivet datum");
		}
		model.put("reqAttr", refreshArchiveInfo(reqAttr));
		
		return "archiving";
	}
	@Autowired
	public void setErrorRepo(ErrorRepository errorRepo) {
		this.errorRepo = errorRepo;
	}
	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	private RequestAttributes refreshArchiveInfo(RequestAttributes reqAttr) {
		reqAttr.setNewErrorMessages(errorRepo.findErrorsByArchived(false).size());
		reqAttr.setActiveCount(orderRepo.countOrdersByStatusList(Arrays.asList(StatusConstants.ACTIVE_STATI)));
		reqAttr.setPassiveCount(orderRepo.countOrdersByStatusList(Arrays.asList(StatusConstants.INACTIVE_STATI)));
		try {
			reqAttr.setFirstDate(DateUtil.dateToString(orderRepo.getFirstDeliveryDate(StatusConstants.ORDER_STATUS_TRANSFERED)));
		} catch (ParseException e) {
			reqAttr.setFirstDate("saknas");
		}
		return reqAttr;
	}
}
