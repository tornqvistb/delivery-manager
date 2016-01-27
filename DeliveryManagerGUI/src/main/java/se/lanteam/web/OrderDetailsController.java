package se.lanteam.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import se.lanteam.domain.OrderComment;
import se.lanteam.domain.OrderHeader;
import se.lanteam.repository.OrderRepository;

@Controller
public class OrderDetailsController {

	private static final String STATUS_MSG_OK = "Statusmeddelande skickat.";
	private static final String STATUS_MSG_COMMENT_MISSING = "Du måste skriva ett meddelande.";

	private OrderRepository orderRepo;	

	@RequestMapping(value = "order-list/view/registerComment/{orderId}", method = RequestMethod.POST)
	public String registerMessage(@ModelAttribute RequestAttributes reqAttr, @PathVariable Long orderId,
			ModelMap model) {
		OrderHeader order = orderRepo.findOne(orderId);
		if (StringUtils.hasText(reqAttr.getComment())) {
			OrderComment comment = new OrderComment();
			comment.setOrderHeader(order);
			comment.setCreationDate(new Date());
			comment.setOrderLine(String.valueOf(reqAttr.getOrderLineId()));
			comment.setMessage(reqAttr.getComment());
			order.getOrderComments().add(comment);
			orderRepo.save(order);
			order = orderRepo.findOne(orderId);
			model.put("order", order);
			reqAttr = new RequestAttributes();
			reqAttr.setStatusMessageCreationSuccess(STATUS_MSG_OK);
			model.put("reqAttr", reqAttr);
		} else {
			model.put("order", order);
			reqAttr = new RequestAttributes();
			reqAttr.setStatusMessageCreationFailed(STATUS_MSG_COMMENT_MISSING);
			model.put("reqAttr", reqAttr);
		}
		return "order-details";
	}

	@Autowired
	public void setOrderRepo(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
}
