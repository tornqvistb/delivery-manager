package se.lanteam.model;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import se.lanteam.domain.OrderHeader;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SearchBean {

	private List<OrderHeader> orderList;

	public List<OrderHeader> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderHeader> orderList) {
		this.orderList = orderList;
	}
	
	
}
