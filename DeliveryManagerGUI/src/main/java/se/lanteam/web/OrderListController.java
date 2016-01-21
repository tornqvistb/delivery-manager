package se.lanteam.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderListController {
	
	@RequestMapping("/")
	public String showStartPage() {
		return "redirect:order-list";
	}
		
	@RequestMapping("order-list")
	public String showOrderList() {
		return "order-list";
	}
	
}
